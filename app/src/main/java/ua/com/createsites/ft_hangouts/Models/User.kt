package ua.com.createsites.ft_hangouts.Models

class User {
	var id: Int? = null
	var name: String? = null
	var phone: String? = null
	var avatar: String? = null

	constructor(id: Int?, name: String, phone: String, avatar: String?) {
		this.id = id
		this.name = name
		this.phone = phone
		this.avatar = avatar
	}
}