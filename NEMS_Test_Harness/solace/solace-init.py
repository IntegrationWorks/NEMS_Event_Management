import requests
import time
import json
import os 

from requests.auth import HTTPBasicAuth

class Subscription:
    def __init__(self, topic):
        self.topic = topic


class Queue:
    def __init__(self, name, access_type, maxMsgSpoolUsage, permission, ingress_enabled, egress_enabled, subscribed_topics):
        self.name = name
        self.access_type = access_type
        self.maxMsgSpoolUsage = maxMsgSpoolUsage
        self.permission = permission
        self.ingress_enabled = ingress_enabled
        self.egress_enabled = egress_enabled
        self.subscribed_topics = subscribed_topics

r = requests.Response()
r.status_code = 400

dir_path = os.path.dirname(os.path.realpath(__file__))
json_file_path = dir_path + os.sep + "queues.json"

host_name = "solace"
port = "8080"
msg_vpn_name = "default"

username = "admin"
password = "admin"

basic = HTTPBasicAuth(username, password)

queues = [
    # Queue(name = "TestQueue",
    #       access_type = "exclusive", 
    #       maxMsgSpoolUsage = 5000,
    #       permission = "consume", 
    #       ingress_enabled = True, 
    #       egress_enabled = True,
    #       subscribed_topics=
    #       [
    #           Subscription("test/topic")
    #       ]),
    # Queue(name = "Death",
    #       access_type = "exclusive", 
    #       maxMsgSpoolUsage = 5000,
    #       permission = "consume", 
    #       ingress_enabled = True, 
    #       egress_enabled = True,
    #       subscribed_topics=
    #       [
    #           Subscription("root/nems/death")
    #       ]),
    # Queue(name = "Birth",
    #       access_type = "exclusive", 
    #       maxMsgSpoolUsage = 5000,
    #       permission = "consume", 
    #       ingress_enabled = True, 
    #       egress_enabled = True,
    #       subscribed_topics=
    #       [
    #           Subscription("root/nems/birth")
    #       ]),
    # Queue(name = "Enrollment",
    #       access_type = "exclusive", 
    #       maxMsgSpoolUsage = 5000,
    #       permission = "consume", 
    #       ingress_enabled = True, 
    #       egress_enabled = True,
    #       subscribed_topics=
    #       [
    #           Subscription("root/nems/enrollment")
    #       ])
]

print(dir_path)
try:
    with open(json_file_path, mode="r", encoding="utf-8") as read_file:
        print("file open successful")
        json_data = json.load(read_file)
        print("json data assign successful")
    
    for queue in json_data["queues"]:
            print(queue["name"])
            subscriptions = []
            for sub in queue["subscribed_topics"]:
                subscriptions.append(Subscription(sub["name"]))
            queues.append(
                Queue(
                    name=queue["name"],
                    access_type=queue["access_type"],
                    maxMsgSpoolUsage=queue["maxMsgSpoolUsage"],
                    permission=queue["permission"],
                    ingress_enabled=queue["ingress_enabled"],
                    egress_enabled=queue["egress_enabled"],
                    subscribed_topics= subscriptions
                )
            )

except:
    print("error encountered parsing json file")



while r.status_code != 200 :
    
    print("Attempting connection to Solace Event Broker...")
    try:
        r = requests.get(url="http://" + host_name + ":" + port + "/SEMP/v2/config/msgVpns/" + msg_vpn_name + "/queues", auth=basic)
    except: 
        print("Connection Failed, retrying...")
        time.sleep(1)

print("Connection successful!")

content_header = {'Content-Type': 'application/json'}

for queue in queues:

    print("Sending request to create queue: " + queue.name)

    print("\n -------- \n")

    queue_req_payload = {
        'queueName': queue.name,
        'permission': queue.permission,
        'accessType': queue.access_type,
        'maxMsgSpoolUsage': queue.maxMsgSpoolUsage,
        'ingressEnabled': queue.ingress_enabled,
        'egressEnabled': queue.egress_enabled
    }
    



    req_url = "http://" + host_name + ":" + port + "/SEMP/v2/config/msgVpns/" + msg_vpn_name + "/queues"

    queue_req = requests.post(url=req_url, json = queue_req_payload, headers = content_header, auth=basic)

    print(queue_req.text)

    queue_check = requests.get(url="http://" + host_name + ":" + port + "/SEMP/v2/config/msgVpns/" + msg_vpn_name + "/queues/" + queue.name, auth=basic)

    while(queue_check.status_code != 200):

        print(queue_check.status_code)

        queue_check = requests.get(url="http://" + host_name + ":" + port + "/SEMP/v2/config/msgVpns/" + msg_vpn_name + "/queues/" + queue.name, auth = basic)
    
    print("\n -------- \n")
    print("Queue creation successful! : " + queue.name)

    for subscribed_topic in queue.subscribed_topics:

        print("Sending request to create subscribed topic: " + subscribed_topic.topic + " --- for queue: " + queue.name)


        topic_req_payload = {
        'subscriptionTopic': subscribed_topic.topic
        }
    
        req_url = "http://" + host_name + ":" + port + "/SEMP/v2/config/msgVpns/" + msg_vpn_name + "/queues/" + queue.name + "/subscriptions"

        topic_req = requests.post(url=req_url, 
                                    json=topic_req_payload,
                                    headers= content_header,
                                    auth=basic)
        
        print(topic_req.text)
        
print("Queues and Topic creation finished")