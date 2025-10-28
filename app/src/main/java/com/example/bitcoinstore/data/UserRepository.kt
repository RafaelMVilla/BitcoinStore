// app/src/main/java/com/example/bitcoinstore/data/repo/UserRepository.kt
package com.example.bitcoinstore.data.repo

import com.example.bitcoinstore.data.local.UserDao
import com.example.bitcoinstore.data.local.UserEntity
import com.example.bitcoinstore.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class UserRepository(private val userDao: UserDao) {

    suspend fun register(name: String, email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            if (name.isBlank()) return@withContext Result.failure(IllegalArgumentException("Informe o nome"))
            if (!email.contains("@")) return@withContext Result.failure(IllegalArgumentException("E-mail inválido"))
            if (password.length < 6) return@withContext Result.failure(IllegalArgumentException("Senha mínima de 6 caracteres"))

            val existing = userDao.getByEmail(email)
            if (existing != null) return@withContext Result.failure(IllegalStateException("E-mail já cadastrado"))

            val hashed = hash(password)
            userDao.insert(UserEntity(email = email.trim(), name = name.trim(), passwordHash = hashed))
            Result.success(User(email.trim(), name.trim()))
        }

    suspend fun login(email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            val user = userDao.getByEmail(email) ?: return@withContext Result.failure(IllegalArgumentException("Usuário não encontrado"))
            if (user.passwordHash != hash(password)) {
                return@withContext Result.failure(IllegalArgumentException("Senha inválida"))
            }
            Result.success(User(user.email, user.name))
        }

    suspend fun getUser(email: String): User? = withContext(Dispatchers.IO) {
        userDao.getByEmail(email)?.let { User(it.email, it.name) }
    }

    suspend fun updateName(email: String, newName: String) = withContext(Dispatchers.IO) {
        userDao.updateName(email, newName.trim())
    }

    suspend fun updatePassword(email: String, newPassword: String) = withContext(Dispatchers.IO) {
        userDao.updatePasswordHash(email, hash(newPassword))
    }

    private fun hash(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
