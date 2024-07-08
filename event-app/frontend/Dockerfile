# Stage 1: Build the React app
FROM node:14 as build

WORKDIR /usr/src/app

COPY package*.json ./
RUN npm install

COPY . .

RUN npm run build

# Stage 2: Serve the app
FROM node:14

WORKDIR /usr/src/app

COPY --from=build /usr/src/app/build ./build

RUN npm install -g serve

EXPOSE 3000

CMD ["serve", "-s", "build"]
