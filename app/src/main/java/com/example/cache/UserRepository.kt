package com.example.cache


class UserRepository(private val userDao: UserDao, private val firestore: FirebaseFirestore) {

    val allUsers: LiveData<List<User>> = userDao.getAllUsers()

    suspend fun insert(user: User) {
        userDao.insertUser(user)
        // Salve também no Firestore
        firestore.collection("users").add(user)
    }

    fun getFirestoreUsers(): Task<QuerySnapshot> {
        // Obtenha os usuários do Firestore
        return firestore.collection("users").get()
    }
}
