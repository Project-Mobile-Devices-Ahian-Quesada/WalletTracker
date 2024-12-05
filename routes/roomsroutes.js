'use strict';
var _ = require('underscore');

module.exports = function(app) {
  var rooms = require('../controllers/RoomController');
  var auth = require('../controllers/AuthController');

  // todoList Routes
  app.route('/rooms')
    .get(rooms.list_all_rooms)
  
  app.route('/rooms/booking')
    .put(rooms.booking)
  
  app.route('/rooms/unbooking')
    .put(rooms.unbooking)
    // .post(rooms.create_a_room)
    // .put(rooms.update_a_room);

    // app.route('/rooms/updateall')
    // .put(rooms.update_rooms);

  app.route('/rooms/:roomName')
     .get(rooms.read_a_room)
    //  .delete(rooms.delete_a_room);

  //app.route('/api/auth/:username/:password')
  app.route('/users/auth')
     .post(auth.authUser);
  
  app.route('/users')
     .get(auth.getUsers);
};