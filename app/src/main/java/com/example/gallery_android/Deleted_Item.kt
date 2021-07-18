package com.example.gallery_android

class Deleted_Item {
    var feedID:String?=null
    var feedtitle:String?=null
    var feedImageURL:String?=null
    var feeddes:String?=null
    var deleted_time:String?=null
    constructor(feedID:String,feedtitle:String,feeddes:String,feedImageURL:String, deleted_time:String){
        this.feedID=feedID
        this.feedtitle=feedtitle
        this.feedImageURL=feedImageURL
        this.feeddes=feeddes
        this. deleted_time= deleted_time
    }


}