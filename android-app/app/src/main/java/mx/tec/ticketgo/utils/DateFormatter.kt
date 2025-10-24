package mx.tec.ticketgo.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    
    // Zona horaria UTC para fechas del servidor
    private val utcTimeZone = TimeZone.getTimeZone("UTC")
    
    // Zona horaria local del dispositivo
    private val localTimeZone = TimeZone.getDefault()
    
    private val inputFormats = listOf(
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply { timeZone = utcTimeZone },
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault()).apply { timeZone = utcTimeZone },
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply { timeZone = utcTimeZone },
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply { timeZone = utcTimeZone },
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply { timeZone = utcTimeZone },
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply { timeZone = utcTimeZone }
    )
    
    private val outputFormat = SimpleDateFormat("d MMM yyyy h:mm a", Locale("es", "MX"))
    
    private val monthNames = mapOf(
        1 to "ene", 2 to "feb", 3 to "mar", 4 to "abr",
        5 to "may", 6 to "jun", 7 to "jul", 8 to "ago",
        9 to "sept", 10 to "oct", 11 to "nov", 12 to "dic"
    )
    
    fun formatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "Sin fecha"
        
        return try {
            // Intentar parsear con diferentes formatos (asumiendo UTC)
            var parsedDate: Date? = null
            for (format in inputFormats) {
                try {
                    parsedDate = format.parse(dateString)
                    break
                } catch (e: Exception) {
                    // Continuar con el siguiente formato
                }
            }
            
            if (parsedDate != null) {
                // Convertir de UTC a zona horaria local
                val calendar = Calendar.getInstance(localTimeZone)
                calendar.time = parsedDate
                
                // NO ajustar manualmente el offset - Calendar ya maneja la conversión de zona horaria
                
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = monthNames[calendar.get(Calendar.MONTH) + 1] ?: "ene"
                val year = calendar.get(Calendar.YEAR)
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val ampm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "am" else "pm"
                
                // Convertir a formato 12 horas
                val displayHour = when {
                    hour == 0 -> 12
                    hour > 12 -> hour - 12
                    else -> hour
                }
                
                "$day $month $year $displayHour:${String.format("%02d", minute)} $ampm"
            } else {
                dateString // Devolver original si no se puede parsear
            }
        } catch (e: Exception) {
            dateString // Devolver original en caso de error
        }
    }
    
    fun formatDateShort(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "Sin fecha"
        
        return try {
            // Intentar parsear con diferentes formatos (asumiendo UTC)
            var parsedDate: Date? = null
            for (format in inputFormats) {
                try {
                    parsedDate = format.parse(dateString)
                    break
                } catch (e: Exception) {
                    // Continuar con el siguiente formato
                }
            }
            
            if (parsedDate != null) {
                // Convertir de UTC a zona horaria local
                val calendar = Calendar.getInstance(localTimeZone)
                calendar.time = parsedDate
                
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = monthNames[calendar.get(Calendar.MONTH) + 1] ?: "ene"
                val year = calendar.get(Calendar.YEAR)
                
                "$day $month $year"
            } else {
                dateString
            }
        } catch (e: Exception) {
            dateString
        }
    }
    
    fun formatNotificationDate(dateString: String?): Pair<String, String> {
        if (dateString.isNullOrEmpty()) return Pair("Sin fecha", "")
        
        return try {
            // Intentar parsear con diferentes formatos (asumiendo UTC)
            var parsedDate: Date? = null
            for (format in inputFormats) {
                try {
                    parsedDate = format.parse(dateString)
                    break
                } catch (e: Exception) {
                    // Continuar con el siguiente formato
                }
            }
            
            if (parsedDate != null) {
                // Convertir de UTC a zona horaria local
                val calendar = Calendar.getInstance(localTimeZone)
                calendar.time = parsedDate
                
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = monthNames[calendar.get(Calendar.MONTH) + 1] ?: "ene"
                val year = calendar.get(Calendar.YEAR)
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val ampm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "am" else "pm"
                
                // Convertir a formato 12 horas
                val displayHour = when {
                    hour == 0 -> 12
                    hour > 12 -> hour - 12
                    else -> hour
                }
                
                val date = "$day $month $year"
                val time = "$displayHour:${String.format("%02d", minute)} $ampm"
                
                Pair(date, time)
            } else {
                Pair(dateString, "")
            }
        } catch (e: Exception) {
            Pair(dateString, "")
        }
    }
}
