// app/src/main/java/com/example/bitcoinstore/ui/home/ProductCatalog.kt
package com.example.bitcoinstore.ui.home

import com.example.bitcoinstore.R

data class Product(
    val id: Int,
    val name: String,
    val priceBRL: Double,
    val image: Int,
    val description: String,
    val tags: List<String> = emptyList(),
    val rating: Double = 4.6 // 0.0..5.0
)

object ProductCatalog {
    private val items = listOf(
        Product(
            id = 1,
            name = "Carteira de Bitcoin",
            priceBRL = 499.00,
            image = R.drawable.prod1,
            description = "Carteira física resistente, ideal para quem busca guardar suas chaves com segurança e praticidade no dia a dia.",
            tags = listOf("Segurança", "Acessórios"),
            rating = 4.7
        ),
        Product(
            id = 2,
            name = "Camisa Bitcoin",
            priceBRL = 149.00,
            image = R.drawable.prod2,
            description = "Camiseta premium 100% algodão com estampas exclusivas para quem vive o espírito do ₿itcoin.",
            tags = listOf("Vestuário", "Estilo"),
            rating = 4.5
        ),
        Product(
            id = 3,
            name = "Boné Crypto",
            priceBRL = 89.90,
            image = R.drawable.prod3,
            description = "Boné casual com acabamento diferenciado e logo minimalista. Combina com qualquer setup.",
            tags = listOf("Vestuário", "Lifestyle"),
            rating = 4.4
        ),
        Product(
            id = 4,
            name = "Mousepad Blockchain",
            priceBRL = 69.90,
            image = R.drawable.prod4,
            description = "Mousepad de superfície microtexturizada, borda costurada e base emborrachada para precisão máxima.",
            tags = listOf("Setup", "Escritório"),
            rating = 4.3
        ),
        Product(
            id = 5,
            name = "Jade Coldwallet",
            priceBRL = 555.00,
            image = R.drawable.prod5,
            description = "Coldwallet focada em segurança, com proteção extra para suas chaves privadas e backup simplificado.",
            tags = listOf("Segurança", "Hardware Wallet"),
            rating = 4.8
        ),
        Product(
            id = 6,
            name = "Caneca de Bitcoin",
            priceBRL = 35.00,
            image = R.drawable.prod6,
            description = "Caneca 350ml em cerâmica com estampa resistente. Perfeita para o café do HODL matinal.",
            tags = listOf("Cozinha", "Lifestyle"),
            rating = 4.6
        ),
        Product(
            id = 7,
            name = "Stackbit Metal Wallet",
            priceBRL = 250.00,
            image = R.drawable.prod7,
            description = "Placas de aço para backup de seed phrase. Resistência a fogo e impacto para máxima segurança.",
            tags = listOf("Segurança", "Backup"),
            rating = 4.9
        ),
        Product(
            id = 8,
            name = "Bitcoin o Código da Liberdade",
            priceBRL = 75.90,
            image = R.drawable.prod8,
            description = "Livro didático e direto ao ponto para entender os fundamentos do Bitcoin e sua filosofia.",
            tags = listOf("Livro", "Educação"),
            rating = 4.7
        ),
        Product(
            id = 9,
            name = "Quadro Bitcoin",
            priceBRL = 99.90,
            image = R.drawable.prod9,
            description = "Quadro decorativo minimalista para destacar seu espaço com estilo ₿itcoin.",
            tags = listOf("Decoração", "Minimalista"),
            rating = 4.5
        ),
        Product(
            id = 10,
            name = "Tênis Bitcoin",
            priceBRL = 325.00,
            image = R.drawable.prod10,
            description = "Tênis leve e confortável com detalhes temáticos. Perfeito para o dia a dia.",
            tags = listOf("Vestuário", "Lifestyle"),
            rating = 4.4
        ),
    )

    fun all(): List<Product> = items
    fun get(id: Int): Product = items.first { it.id == id }
}
