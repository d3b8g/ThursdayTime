package net.d3b8g.thursdaytime.app.main.components

import net.d3b8g.thursdaytime.app.main.module.Report
import net.d3b8g.thursdaytime.ui.adapters.ProductsAdapter

class Components {
    companion object{
        const val sSetting = "setup"
        const val sReport = "reports"
        const val sNameHash = "hash"
        const val sNameDevice = "device.id"
        val severity_name = listOf("Уязвимость","Критический","Высокий","Средний","Низкий")
        val issue_type_name = listOf("Падение приложения","Зависание приложения","Неработающая функциональность","Потеря данных","Производительность","Косметическое несоответствие ","Ошибка в тексте","Пожелание")
        val severityColor = listOf<Int>()

        var ids:ArrayList<Int> = ArrayList()
        var reports:ArrayList<Report> = ArrayList()
    }
}