package ua.com.createsites.ft_hangouts.Models

data class SmsData(val senderName: String?, val senderPhone: String, val message: String, val date: String, val read: Int, val seen: Int, val type: Int)
