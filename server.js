var express = require('express'),
  app = express(),
  port = process.env.PORT || 5001,
  bodyParser = require('body-parser');

const cors = require('cors');

// Configuring body parser middleware
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(cors());

var routes = require('./routes/salasroutes'); //importing route
routes(app); //register the route

app.listen(port);

console.log('todo list RESTful API server started on: ' + port);