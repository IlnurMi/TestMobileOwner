package com.ilnur.mobileowner.domain.models.request

import com.google.gson.annotations.SerializedName

class Objects(@SerializedName("firstName")
              val firstName:String,
              @SerializedName("secondName")
              val secondName:String,
              @SerializedName("lastName")
              val lastName:String,
              @SerializedName("name")
              val name:String,
              @SerializedName("full_name")
              val fullName:String,
              @SerializedName("birthday")
              val birthday:String,
              @SerializedName("factAddress")
              val factAddress:String,
              @SerializedName("registrationAddress")
              val registrationAddress:String,
              @SerializedName("passport")
              val passport:String,
              @SerializedName("checkingAccount")
              val checkingAccount:String,
              @SerializedName("photo")
              val photo:String,
              @SerializedName("inn")
              val inn:String,
              @SerializedName("kpp")
              val kpp:String,
              @SerializedName("ogrn")
              val ogrn:String,
              @SerializedName("id")
              val id:String,
              @SerializedName("company")
              val company:String,
              @SerializedName("description")
              val description:String,
              @SerializedName("isJuridicalPerson")
              val isJuridicalPerson:String,
              @SerializedName("createDate")
              val createDate:String,
              @SerializedName("modifyDate")
              val modifyDate:String,
              @SerializedName("role")
              val role:String) {
}