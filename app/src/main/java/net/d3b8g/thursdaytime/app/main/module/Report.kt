package net.d3b8g.thursdaytime.app.main.module

data class Report(
    var id:Int,
    var title:String,
    var steps:String,
    var sa:String,
    var ss:String,
    var severity:Int,
    var issue_type:Int
)