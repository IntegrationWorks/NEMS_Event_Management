# Use an official Node.js image
FROM node:14

# Create app directory
WORKDIR /usr/src/app

# Install app dependencies
COPY package*.json ./
RUN npm install

# Bundle app source
COPY . .

# Expose the port the app runs on
EXPOSE 5000

# Define environment variable
ENV PORT=5000

# Run the app
CMD ["node", "index.js"]
