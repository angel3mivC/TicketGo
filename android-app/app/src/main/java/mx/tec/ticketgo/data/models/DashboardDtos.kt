package mx.tec.ticketgo.data.models

data class Summary(
    val total: Int,
    val abiertos: Int,
    val cerrados: Int
)

data class TotalTickets(
    val total: Int
)

data class OpenTickets(
    val abiertos: Int
)

data class ClosedTickets(
    val cerrados: Int
)

data class MyTickets(
    val mis_tickets: Int
)