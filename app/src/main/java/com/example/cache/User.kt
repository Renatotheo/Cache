package com.example.cache


@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val sobrenome: String,
    val genero: String,
    val idade: Int,
    val altura: Double,
    val peso: Double,
    val email: String,
    val cidade: String,
    val telefone: String,
    val esportePrincipal: String
)
