package com.example.cache

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class UserRepository(private val userDao: UserDao, private val firestore: FirebaseFirestore) {

    val allUsers: LiveData<List<User>> = userDao.getAllUsers()

    suspend fun getInitialUserData(): List<User> {
        return userDao.getInitialUserData()
    }

    suspend fun insert(user: User) {
        // Insere no Room
        userDao.insertUser(user)

        // Salva também no Firestore
        val firestoreUser = hashMapOf(
            "nome" to user.nome,
            "sobrenome" to user.sobrenome,
            "genero" to user.genero,
            "idade" to user.idade,
            "altura" to user.altura,
            "peso" to user.peso,
            "email" to user.email,
            "cidade" to user.cidade,
            "telefone" to user.telefone,
            "esportePrincipal" to user.esportePrincipal
        )

        firestore.collection("users").add(firestoreUser)
    }

    fun getFirestoreUsers(): Task<QuerySnapshot> {
        return firestore.collection("users").get()
    }
}
