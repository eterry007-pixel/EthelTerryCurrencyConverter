package com.example.ethelterrycurrencyconverter

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ethelterrycurrencyconverter.data.RetrofitInstance
import kotlinx.coroutines.launch

class CurrencyViewModel : ViewModel() {
    private val _rates = mutableStateOf<Map<String, Double>>(emptyMap())
    val rates: State<Map<String, Double>> = _rates

    private val _availableCurrencies = mutableStateOf(listOf("USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CNY", "INR"))
    val availableCurrencies: State<List<String>> = _availableCurrencies

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _backgroundColor = mutableStateOf(Color.White)
    val backgroundColor: State<Color> = _backgroundColor

    private val _isMusicEnabled = mutableStateOf(false)
    val isMusicEnabled: State<Boolean> = _isMusicEnabled

    // Language state and list
    private val _language = mutableStateOf("English")
    val language: State<String> = _language

    val supportedLanguages = listOf(
        "English", "Spanish", "French", "German", "Chinese", 
        "Japanese", "Korean", "Italian", "Portuguese", "Russian"
    )

    //main screen country currency options
    private val _fromCurrency = mutableStateOf("USD")
    val fromCurrency: State<String> = _fromCurrency

    private val _toCurrency = mutableStateOf("EUR")
    val toCurrency: State<String> = _toCurrency

    private val _amount = mutableStateOf("1.0")
    val amount: State<String> = _amount

    private val _conversionResult = mutableStateOf("")
    val conversionResult: State<String> = _conversionResult

    init {
        fetchRates("USD")
    }

    fun fetchRates(base: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getRates(base)
                _rates.value = response.rates
                _availableCurrencies.value = response.rates.keys.toList().sorted()
                calculateConversion()
            } catch (e: Exception) {
                // Keep default
            } finally {
                _isLoading.value = false
            }
        }
    }

    //currency convertor calculator
    fun setAmount(newAmount: String) {
        _amount.value = newAmount
        calculateConversion()
    }

    fun setFromCurrency(currency: String) {
        _fromCurrency.value = currency
        calculateConversion()
    }

    fun setToCurrency(currency: String) {
        _toCurrency.value = currency
        calculateConversion()
    }

    fun calculateConversion() {
        val ratesMap = _rates.value
        if (ratesMap.isEmpty()) return

        val fromRate = ratesMap[_fromCurrency.value] ?: 1.0
        val toRate = ratesMap[_toCurrency.value] ?: 1.0
        val inputAmount = _amount.value.toDoubleOrNull() ?: 0.0

        val result = (inputAmount / fromRate) * toRate
        _conversionResult.value = String.format("%.2f", result)
    }

    //main screen background colour option and music option
    fun setBackgroundColor(color: Color) {
        _backgroundColor.value = color
    }

    fun toggleMusic() {
        _isMusicEnabled.value = !_isMusicEnabled.value
    }

    fun setLanguage(lang: String) {
        _language.value = lang
    }

    // Simple translation helper
    fun translate(key: String): String {
        val translations = mapOf(
            "English" to mapOf(
                "title" to "Currency Converter",
                "amount" to "Amount",
                "from" to "From:",
                "to" to "To:",
                "convert" to "Convert",
                "result" to "Result:",
                "settings" to "Settings",
                "bg_color" to "Background Color",
                "music" to "Music",
                "language" to "Language",
                "white" to "White",
                "gray" to "Gray"
            ),
            "Spanish" to mapOf(
                "title" to "Conversor de Divisas",
                "amount" to "Cantidad",
                "from" to "De:",
                "to" to "A:",
                "convert" to "Convertir",
                "result" to "Resultado:",
                "settings" to "Ajustes",
                "bg_color" to "Color de Fondo",
                "music" to "Música",
                "language" to "Idioma",
                "white" to "Blanco",
                "gray" to "Gris"
            ),
            "French" to mapOf(
                "title" to "Convertisseur de Devises",
                "amount" to "Montant",
                "from" to "De:",
                "to" to "À:",
                "convert" to "Convertir",
                "result" to "Résultat:",
                "settings" to "Paramètres",
                "bg_color" to "Couleur de Fond",
                "music" to "Musique",
                "language" to "Langue",
                "white" to "Blanc",
                "gray" to "Gris"
            ),
            "German" to mapOf(
                "title" to "Währungsrechner",
                "amount" to "Betrag",
                "from" to "Von:",
                "to" to "Nach:",
                "convert" to "Umrechnen",
                "result" to "Ergebnis:",
                "settings" to "Einstellungen",
                "bg_color" to "Hintergrundfarbe",
                "music" to "Musik",
                "language" to "Sprache",
                "white" to "Weiß",
                "gray" to "Grau"
            ),
            "Chinese" to mapOf(
                "title" to "货币转换器",
                "amount" to "金额",
                "from" to "从:",
                "to" to "到:",
                "convert" to "转换",
                "result" to "结果:",
                "settings" to "设置",
                "bg_color" to "背景颜色",
                "music" to "音乐",
                "language" to "语言",
                "white" to "白色",
                "gray" to "灰色"
            ),
            "Japanese" to mapOf(
                "title" to "通貨換算機",
                "amount" to "金額",
                "from" to "から:",
                "to" to "まで:",
                "convert" to "換算",
                "result" to "結果:",
                "settings" to "設定",
                "bg_color" to "背景色",
                "music" to "音楽",
                "language" to "言語",
                "white" to "白",
                "gray" to "グレー"
            ),
            "Korean" to mapOf(
                "title" to "환율 계산기",
                "amount" to "금액",
                "from" to "에서:",
                "to" to "로:",
                "convert" to "변환",
                "result" to "결과:",
                "settings" to "설정",
                "bg_color" to "배경색",
                "music" to "음악",
                "language" to "언어",
                "white" to "화이트",
                "gray" to "그레이"
            ),
            "Italian" to mapOf(
                "title" to "Convertitore di Valuta",
                "amount" to "Importo",
                "from" to "Da:",
                "to" to "A:",
                "convert" to "Converti",
                "result" to "Risultato:",
                "settings" to "Impostazioni",
                "bg_color" to "Colore di Sfondo",
                "music" to "Musica",
                "language" to "Lingua",
                "white" to "Bianco",
                "gray" to "Grigio"
            ),
            "Portuguese" to mapOf(
                "title" to "Conversor de Moedas",
                "amount" to "Valor",
                "from" to "De:",
                "to" to "Para:",
                "convert" to "Converter",
                "result" to "Resultado:",
                "settings" to "Configurações",
                "bg_color" to "Cor de Fundo",
                "music" to "Música",
                "language" to "Idioma",
                "white" to "Branco",
                "gray" to "Cinza"
            ),
            "Russian" to mapOf(
                "title" to "Конвертер валют",
                "amount" to "Сумма",
                "from" to "Из:",
                "to" to "В:",
                "convert" to "Конвертировать",
                "result" to "Результат:",
                "settings" to "Настройки",
                "bg_color" to "Цвет фона",
                "music" to "Музыка",
                "language" to "Язык",
                "white" to "Белый",
                "gray" to "Серый"
            )
        )
        return translations[_language.value]?.get(key) ?: key
    }
}
