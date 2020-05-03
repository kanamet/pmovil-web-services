package com.pmovil.webservices.model

class Pokemon(
    var number: Int, var name: String, var types: List<String>, var pictureId: String
) {
    constructor() : this(0, "", emptyList(), "")
}