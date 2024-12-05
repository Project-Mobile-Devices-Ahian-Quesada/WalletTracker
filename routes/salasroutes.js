'use strict';
module.exports = function(app) {
  var rooms = require('../controllers/salascontroller');
  var auth = require('../controllers/AuthController');

  // todoList Routes
  app.route('/api/rooms')
    .get(rooms.list_all_rooms)
    .post(rooms.create_a_room)
    .put(rooms.update_a_room);

    app.route('/api/rooms/updateall')
    .put(rooms.update_rooms);

  app.route('/api/rooms/:roomName')
     .get(rooms.read_a_room)
     .delete(rooms.delete_a_room);

  //app.route('/api/auth/:username/:password')
  app.route('/api/auth')
     .post(auth.authUser);
};