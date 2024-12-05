'use strict';

const rooms =  [
    {
        room: "CR1-100",
        capacity: 6,
        isBusy: false,
        start: null,
        end: null
    }
];

exports.list_all_rooms = function(req, res) {  
    res.status(200).send(rooms);
    //res.json(rooms);
};

exports.read_a_room = function(req, res) {
    let room = rooms.filter(item => item.room === req.params.roomName);

    if (room.length > 0){
        res.status(200).send(room);
    }else
        res.status(404).send({ success: 'false', message: 'Room not found' });
    //res.json(room);
};

exports.create_a_room = function(req, res) {
    if (req.body.room === undefined || req.body === null || req.body.length === 0 || req.body === ''){
        res.status(404).send({ success: 'false', message: 'Must provide the room info to save it.' });
        return;
    }

    let room = rooms.filter(item => item.room === req.body.room);
    if (room.length > 0){
        res.status(404).send({ success: 'false', message: 'The room is already stored. Specify another one.' });
        return;
    }

    let newRoom = {
        room: req.body.room,
        capacity: parseInt(req.body.capacity),
        isBusy: false,
        start: null,
        end: null
    };
    rooms.push(newRoom);
    res.status(200).send(rooms);
};

exports.update_a_room = function(req, res) {
    if (req.body.room === undefined || req.body === null || req.body.length === 0 || req.body === ''){
        res.status(404).send({ success: 'false', message: 'Must provide the room info to save it.' });
        return;
    }

    const index = rooms.findIndex(x => x.room === req.body.room);
    if (index === undefined || index === -1){
        res.status(404).send({ success: 'false', message: 'The room does not exist. Specify a room that is already stored.' });
        return;
    }
    rooms.splice(index, 1);

    let newRoom = {
        room: req.body.room,
        capacity: parseInt(req.body.capacity),
        isBusy: req.body.isBusy === "false",
        start: req.body.start === "null" ? null :  req.body.start,
        end: req.body.end === "null" ? null :  req.body.end
    };
    rooms.push(newRoom);
    res.status(200).send(rooms);
};

exports.delete_a_room = function(req, res) {
    if (req.params.roomName === undefined || req.params.roomName === null || req.params.roomName.length === 0){
        res.status(404).send({ success: 'false', message: 'Must provide the room to delete it.' });
        return;
    }

    const index = rooms.findIndex(x => x.room === req.params.roomName);
    if (index === undefined || index === -1){
        res.status(404).send({ success: 'false', message: 'The room does not exist. Specify a room that is already stored.' });
        return;
    }
    rooms.splice(index, 1);
    res.status(200).send(rooms);
};

exports.update_rooms = function(req, res) {
    if (req.body === undefined || req.body === null || req.body.length === 0 || req.body === ''){
        res.status(404).send({ success: 'false', message: 'Must provide the rooms info to save them.' });
        return;
    }

    let tempRooms=[];
    req.body.forEach(element => {
        let newRoom = {
            room: element.room,
            capacity: parseInt(element.capacity),
            isBusy: element.isBusy === "true" || element.isBusy,
            start: element.start === "null" || element.start === null ? null :  element.start,
            end: element.end === "null" || element.end === null ? null :  element.end
        };    
        tempRooms.push(newRoom);
    });
    
    rooms.splice(0, rooms.length);
    tempRooms.forEach(element => {
        rooms.push(element);
    });
    res.status(200).send(rooms);
};