# Use an official Node runtime as a base image
FROM node:21-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the package.json and package-lock.json files to the working directory
COPY ./hello-world-client/package*.json ./

# Install dependencies
RUN npm install

# Copy the contents of the source directory to the working directory
COPY ./hello-world-client .

# Expose port 3000 to the outside world
EXPOSE 3000

# Command to run the application
CMD ["npm", "start"]