package com.example.cache

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.cache.ui.theme.CacheTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyApp.database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).build()

        userRepository = UserRepository(MyApp.database.userDao(), FirebaseFirestore.getInstance())

        val cache = getSharedPreferences("cache", MODE_PRIVATE)
        val isFirstTime = cache.getBoolean("isFirstTime", true)

        if (isFirstTime) {
            // Consulta inicial ao banco de dados
            CoroutineScope(Dispatchers.IO).launch {
                val initialUserData = userRepository.getInitialUserData()
                if (initialUserData.isEmpty()) {
                    // O banco de dados está vazio, você pode realizar ações iniciais aqui
                    // Por exemplo, inserir dados de amostra
                    val sampleUser = User(
                        id = 0,
                        nome = "John",
                        sobrenome = "Doe",
                        genero = "Masculino",
                        idade = 25,
                        altura = 175.0,
                        peso = 70.0,
                        email = "john.doe@example.com",
                        cidade = "Cidade Exemplo",
                        telefone = "123456789",
                        esportePrincipal = "Futebol"
                    )
                    userRepository.insert(sampleUser)
                }

                // Atualiza o cache
                cache.edit().putBoolean("isFirstTime", false).apply()
            }
        }

        val okButton = findViewById<Button>(R.id.okButton)
        okButton.setOnClickListener {
            // Obtenha os dados do EditText
            val nome = findViewById<EditText>(R.id.nomeEditText).text.toString()
            val sobrenome = findViewById<EditText>(R.id.sobrenomeEditText).text.toString()
            val genero = findViewById<EditText>(R.id.generoEditText).text.toString()
            val idade = findViewById<EditText>(R.id.idadeEditText).text.toString().toIntOrNull() ?: 0
            val altura = findViewById<EditText>(R.id.alturaEditText).text.toString().toDoubleOrNull() ?: 0.0
            val peso = findViewById<EditText>(R.id.pesoEditText).text.toString().toDoubleOrNull() ?: 0.0
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val cidade = findViewById<EditText>(R.id.cidadeEditText).text.toString()
            val telefone = findViewById<EditText>(R.id.telefoneEditText).text.toString()
            val esportePrincipal = findViewById<EditText>(R.id.esportePrincipalEditText).text.toString()

            // Salve no banco de dados
            val user = User(
                id = 0,
                nome = nome,
                sobrenome = sobrenome,
                genero = genero,
                idade = idade,
                altura = altura,
                peso = peso,
                email = email,
                cidade = cidade,
                telefone = telefone,
                esportePrincipal = esportePrincipal
            )
            CoroutineScope(Dispatchers.IO).launch {
                userRepository.insert(user)

            // Atualize a interface do usuário com os dados inseridos
                withContext(Dispatchers.Main) {
                    // Atualize os TextViews correspondentes
                    findViewById<TextView>(R.id.nomeTitle).text = "Nome: $nome"
                    findViewById<TextView>(R.id.sobrenomeTitle).text = "Sobrenome: $sobrenome"
                    findViewById<TextView>(R.id.generoTitle).text = "Gênero: $genero"
                    findViewById<TextView>(R.id.idadeTitle).text = "Idade: $idade"
                    findViewById<TextView>(R.id.alturaTitle).text = "Altura: $altura"
                    findViewById<TextView>(R.id.pesoTitle).text = "Peso: $peso"
                    findViewById<TextView>(R.id.emailTitle).text = "Email: $email"
                    findViewById<TextView>(R.id.cidadeTitle).text = "Cidade: $cidade"
                    findViewById<TextView>(R.id.telefoneTitle).text = "Telefone: $telefone"
                    findViewById<TextView>(R.id.esportePrincipalTitle).text = "Esporte Principal: $esportePrincipal"
                }
        }
    }
    }
}

