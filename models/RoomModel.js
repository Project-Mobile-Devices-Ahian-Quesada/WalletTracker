'use strict';

var _ = require('underscore');
var util = require('../util/util');
var jsonFile = './data/rooms.json';

let roomsData = JSON.parse(util.readFile(jsonFile));

exports.getById = function(room){
    return roomsData.filter (c=> c.room == room);    
};

exports.get_rooms = function(){
    return roomsData;    
};

exports.update = function(room, isBusy, user, date){
    let message= '';

    let useObj = this.getById(room);
    if (useObj.length > 0){
        roomsData = roomsData.filter((el) => {
            return el.room !== room;
          });
        
        useObj[0]['is_busy']=isBusy;
        useObj[0]['user']=user;
        useObj[0]['date']=date;

        roomsData.push(useObj[0]);
        util.writeFile(JSON.stringify(roomsData), jsonFile);
        return useObj[0];
    }else{     
        return null
    }
};