package com.example.chatnewsfeed
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import java.time.Year
@Parcelize
class FInfo(var user_id: String, var name: String,var no: String, var profileImageUrl: String,var email: String, var clgid: String, var stream: String): Parcelable{
    constructor(): this("","","","","","","")
}



class SInfo(var user_id: String, var name: String,var no: String, var profileImageUrl: String,var email: String, var clgid: String, var stream: String, var div: String, var year: String){
    constructor(): this("","","","","","","","","")
}
