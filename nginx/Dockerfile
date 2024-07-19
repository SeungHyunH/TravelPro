# Use the official Nginx image
FROM nginx:latest

# Set up the working directory
WORKDIR /usr/share/nginx/html

# Remove the default Nginx configuration file
RUN rm /etc/nginx/conf.d/default.conf

# Copy the Nginx configuration file
COPY ./nginx.conf /etc/nginx/nginx.conf

# Set permissions for the HTML directory
RUN chmod -R 755 /usr/share/nginx/html
RUN chown -R nginx:nginx /usr/share/nginx/html

# Copy your static files
COPY ./html /usr/share/nginx/html

# Expose the port Nginx will run on
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

