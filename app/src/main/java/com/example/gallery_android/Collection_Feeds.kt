package com.example.gallery_android

class Collection_Feeds {






        var ListFeeds =ArrayList<Feed>()

        constructor(feedID:ArrayList<Feed>){
            this.ListFeeds=feedID

        }
        fun  get(): ArrayList<Feed>{
            ListFeeds = this.ListFeeds
            return ListFeeds
        }



        // setter
        fun  set(value:ArrayList<Feed>) {
            this.ListFeeds = value
        }



}