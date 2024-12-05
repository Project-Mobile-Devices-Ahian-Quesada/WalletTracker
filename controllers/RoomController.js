'use strict';
var _ = require('underscore');
let model = require('../models/RoomModel');
var httpresponse = require('../util/httpResponse')
var endpointresponse = require('../util/endpointResponse')

var sucess_meg= 'Action executed sucessfully.'

exports.list_all_rooms = function(req, res) {  
    let data = model.get_rooms();
    let result = {
        data: data,
        responseCode: endpointresponse.SUCESSFUL,
        message: sucess_meg
    };
    res.status(httpresponse.OK).send(result);
};

exports.read_a_room = function(req, res) {
    let httpCode=0;
    let result= {};
    
    if (req.params.roomName === undefined || req.params.roomName === null){
        httpCode=httpresponse.BAD_REQUEST
        result = {
            data: null,
            responseCode: endpointresponse.MISSING_PARAMETERS,
            message: 'You must send the room as parameter.'
        };
    }else{
        let room = model.getById(req.params.roomName );
        if (room.length > 0){
            httpCode=httpresponse.OK
            result = {
                data: room[0],
                responseCode: endpointresponse.INFO_FOUND,
                message: sucess_meg
            };
        }else{
            httpCode=httpresponse.BAD_REQUEST
            result = {
                data: null,
                responseCode: endpointresponse.INFO_NOT_FOUND,
                message: 'Room not found.'
            };
        }
    }
    
    res.status(httpCode).send(result);
};

exports.booking = function(req, res){
    let httpCode=0;
    let result= {};
    if (req.body.username === undefined || req.body.room === undefined || req.body === null || req.body.length === 0 || req.body === ''){
        httpCode=httpresponse.BAD_REQUEST
        result = {
            data: null,
            responseCode: endpointresponse.MISSING_PARAMETERS,
            message: 'Must provide the room and the username to book a room.'
        };
    }else{
        let room = model.getById(req.body.room);
        if (room.length > 0){
            if (!room[0]['is_busy']){
                let currentDate = new Date();
                let formattedDate = `${currentDate.getFullYear()}-${currentDate.getMonth() + 1}-${currentDate.getDate()}`;    
                let roomupdated = model.update(req.body.room, true, req.body.username, formattedDate)
                if (roomupdated == null){
                    
                }else{
                    httpCode=httpresponse.OK
                    result = {
                        data: roomupdated,
                        responseCode: endpointresponse.SUCESSFUL,
                        message: sucess_meg
                    };
                }
            }else{
                httpCode=httpresponse.BAD_REQUEST
                result = {
                    data: null,
                    responseCode: endpointresponse.INFO_NOT_FOUND,
                    message: 'The room cannot be booked, it has been booked previously.'
                };
            }            
        }else{
            httpCode=httpresponse.BAD_REQUEST
            result = {
                data: null,
                responseCode: endpointresponse.INFO_NOT_FOUND,
                message: 'Room not found.'
            };
        }
    }
    res.status(httpCode).send(result);
};

exports.unbooking = function(req, res){
    let httpCode=0;
    let result= {};
    if (req.body.room === undefined || req.body === null || req.body.length === 0 || req.body === ''){
        httpCode=httpresponse.BAD_REQUEST
        result = {
            data: null,
            responseCode: endpointresponse.MISSING_PARAMETERS,
            message: 'Must provide the room to cancel the booking.'
        };
    }else{
        let room = model.update(req.body.room, false, '', null)
        if (room == null){
            httpCode=httpresponse.BAD_REQUEST
            result = {
                data: null,
                responseCode: endpointresponse.INFO_NOT_FOUND,
                message: 'Room not found.'
            };
        }else{
            httpCode=httpresponse.OK
            result = {
                data: room,
                responseCode: endpointresponse.SUCESSFUL,
                message: sucess_meg
            };
        }
        
    }
    res.status(httpCode).send(result);
};

// exports.create_a_room = function(req, res) {
//     if (req.body.room === undefined || req.body === null || req.body.length === 0 || req.body === ''){
//         res.status(404).send({ success: 'false', message: 'Must provide the room info to save it.' });
//         return;
//     }

//     let room = rooms.filter(item => item.room === req.body.room);
//     if (room.length > 0){
//         res.status(404).send({ success: 'false', message: 'The room is already stored. Specify another one.' });
//         return;
//     }

//     let newRoom = {
//         room: req.body.room,
//         capacity: parseInt(req.body.capacity),
//         isBusy: false,
//         start: null,
//         end: null
//     };
//     rooms.push(newRoom);
//     res.status(200).send(rooms);
// };

// exports.update_a_room = function(req, res) {
//     if (req.body.room === undefined || req.body === null || req.body.length === 0 || req.body === ''){
//         res.status(404).send({ success: 'false', message: 'Must provide the room info to save it.' });
//         return;
//     }

//     const index = rooms.findIndex(x => x.room === req.body.room);
//     if (index === undefined || index === -1){
//         res.status(404).send({ success: 'false', message: 'The room does not exist. Specify a room that is already stored.' });
//         return;
//     }
//     rooms.splice(index, 1);

//     let newRoom = {
//         room: req.body.room,
//         capacity: parseInt(req.body.capacity),
//         isBusy: req.body.isBusy === "false",
//         start: req.body.start === "null" ? null :  req.body.start,
//         end: req.body.end === "null" ? null :  req.body.end
//     };
//     rooms.push(newRoom);
//     res.status(200).send(rooms);
// };

// exports.delete_a_room = function(req, res) {
//     if (req.params.roomName === undefined || req.params.roomName === null || req.params.roomName.length === 0){
//         res.status(404).send({ success: 'false', message: 'Must provide the room to delete it.' });
//         return;
//     }

//     const index = rooms.findIndex(x => x.room === req.params.roomName);
//     if (index === undefined || index === -1){
//         res.status(404).send({ success: 'false', message: 'The room does not exist. Specify a room that is already stored.' });
//         return;
//     }
//     rooms.splice(index, 1);
//     res.status(200).send(rooms);
// };

// exports.update_rooms = function(req, res) {
//     if (req.body === undefined || req.body === null || req.body.length === 0 || req.body === ''){
//         res.status(404).send({ success: 'false', message: 'Must provide the rooms info to save them.' });
//         return;
//     }

//     let tempRooms=[];
//     req.body.forEach(element => {
//         let newRoom = {
//             room: element.room,
//             capacity: parseInt(element.capacity),
//             isBusy: element.isBusy === "true" || element.isBusy,
//             start: element.start === "null" || element.start === null ? null :  element.start,
//             end: element.end === "null" || element.end === null ? null :  element.end
//         };    
//         tempRooms.push(newRoom);
//     });
    
//     rooms.splice(0, rooms.length);
//     tempRooms.forEach(element => {
//         rooms.push(element);
//     });
//     res.status(200).send(rooms);
// };