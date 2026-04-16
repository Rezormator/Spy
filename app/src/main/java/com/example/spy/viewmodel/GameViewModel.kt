package com.example.spy.viewmodel

import android.os.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class GameState { SETTINGS, REVEAL, GAME, FINISH }

class GameViewModel : ViewModel() {
    var playersCount by mutableIntStateOf(3)
    var spiesCount by mutableIntStateOf(1)
    var gameMinutes by mutableIntStateOf(5)

    var currentState by mutableStateOf(GameState.SETTINGS)
    var roles by mutableStateOf(listOf<String>())
    var currentPlayerIndex by mutableIntStateOf(0)
    var isCardVisible by mutableStateOf(false)
    var timeLeft by mutableLongStateOf(0L)
    var isTimerRunning by mutableStateOf(false)
    var spyNumbers by mutableStateOf(listOf<Int>())

    val locations = mutableStateListOf(
        "Орбітальна станція", "Кафедра ІСТ", "Підпільне казино", "Цирк", "Підводний човен",
        "Антарктична база", "Замок Дракули", "Аеропорт", "Художній музей", "Супермаркет",
        "Нічний клуб", "Поліцейська дільниця", "Психіатрична лікарня", "Пасажирський потяг", "Піратський корабель",
        "Військовий бункер", "Телестудія", "Гірськолижний курорт", "Східний ринок", "Королівський палац",
        "Космічний заповідник", "Атомна електростанція", "Пожежна частина", "Будмайданчик", "Бібліотека",
        "Зоопарк", "Тату-салон", "Борт літака", "В’язниця", "Школа магії",
        "Станція метро", "Опера", "Кіностудія", "Овочебаза", "Автосервіс",
        "Готель", "Посольство", "Офіс IT-компанії", "Парк атракціонів", "Секретна лабораторія",
        "Винний погріб", "Корабель прибульців", "Квітковий магазин", "Сільська дискотека", "Футбольний стадіон",
        "Торговий центр", "Похоронне бюро", "Державна податкова", "Шахта", "Океанаріум",
        "Тренажерний зал", "Дитячий садок", "Банківське сховище", "Круїзний лайнер", "Пляж",
        "Монастир", "Ремонт взуття", "Джаз-клуб", "Гастроном", "Вертолітний майданчик",
        "Будинок престарілих", "Знімальний майданчик", "Парковка", "Аптека", "Хімчистка",
        "Збройовий склад", "Пансіонат", "Нафтова вишка", "Маяк", "Коворкінг",
        "Занедбаний завод", "Квест-кімната", "Музей космонавтики", "Обсерваторія", "Планетарій",
        "Рибальське судно", "Експедиція в джунглях", "Штаб-квартира ООН", "Суд", "Рибний ринок",
        "Антикварна лавка", "Салон краси", "Кондитерська фабрика", "Радіостанція", "Детективне агентство",
        "Автобусна зупинка", "Морг", "Ювелірний магазин", "Притулок для тварин", "Військове училище",
        "Більярдний клуб", "Картинг-центр", "Льодова арена", "Басейн", "Пандус авіаносця",
        "Центральний парк", "Дах хмарочоса", "Кабінет стоматолога", "Пологовий будинок", "Залізничний вокзал",
        "Майстерня художника", "Редакція газети", "Кабінет психолога", "Балетна студія", "Іподром",
        "Боулінг", "Тір", "Скеледром", "Меблевий салон", "Магазин іграшок",
        "Шоу-рум", "Ательє", "Ломбард", "Пункт обміну валют", "Їдальня",
        "Ветеринарна клініка", "Будинок культури", "Сільська рада", "Ферма", "Пасіка",
        "Стайня", "Виноградник", "Млин", "Гідроелектростанція", "Сміттєзвалище",
        "Кладовище", "Катакомби", "Печера", "Оазис у пустелі", "Будиночок у лісі",
        "Мисливська хатина", "Альпійське селище", "Індіанське поселення", "Дзен-монастир", "Ейфелева вежа",
        "Статуя Свободи", "Чорнобильська зона", "Парламент", "Митниця", "Парфумерний завод",
        "М’ясокомбінат", "Автосалон", "Автомийка", "Книжковий ярмарок", "Дендропарк",
        "Ботанічний сад", "Виставка собак", "Зустріч випускників", "Весілля", "Корабель-привид",
        "Станція техогляду", "Заправка", "Склад амазону", "Серверна", "Метробуд",
        "Археологічні розкопки", "Середньовічний ярмарок", "Лицарський турнір", "Гладіаторська арена", "Колізей",
        "Парламентська їдальня", "Музей воскових фігур", "Виставка сучасного мистецтва", "Авторинок", "Барахолка",
        "Притон", "Штаб революціонерів", "Табір скаутів", "Гімалайська вершина", "Палуба яхти",
        "Роздягальня", "Сауна", "Хамам", "Керамічна майстерня", "Ковальство",
        "Зерносховище", "Порт", "Вантажний термінал", "Дзвіниця", "Бункер президента",
        "Підземний бункер", "Майдан незалежності", "Пивзавод", "Ковзанка", "Канатна дорога",
        "Вуличний фестиваль", "Нічна черга в АТБ", "Палата лордів", "Білий дім", "Кремль (палаючий)",
        "Форт Боярд", "Скляний лабіринт", "Дзеркальна кімната", "Шаховий турнір", "Склад піротехніки",
        "Завод іграшок", "Фабрика шоколаду", "Сміттєспалювальний завод", "Річковий трамвай", "Повітряна куля",
        "Дирижабль", "Підземна стоянка", "Пральня", "Комуналка", "Покинуте село"
    )

    fun startGame() {
        val location = locations.random()
        val allRoles = MutableList(playersCount) { location }
        val spyIndices = (0 until playersCount).shuffled().take(spiesCount)
        spyIndices.forEach { allRoles[it] = "SPY" }
        spyNumbers = spyIndices.map { it + 1 }.sorted()

        roles = allRoles
        currentPlayerIndex = 0
        isCardVisible = false
        currentState = GameState.REVEAL
    }

    fun addLocation(name: String) {
        if (name.isNotBlank() && name.length <= 50) {
            locations.add(name)
        }
    }

    fun removeLocation(name: String) {
        if (locations.size > 1) locations.remove(name)
    }

    fun nextPlayer() {
        if (currentPlayerIndex < playersCount - 1) {
            currentPlayerIndex++
            isCardVisible = false
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        timeLeft = gameMinutes * 60L
        isTimerRunning = true
        currentState = GameState.GAME
        viewModelScope.launch {
            while (isTimerRunning && timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            if (timeLeft == 0L && isTimerRunning) currentState = GameState.FINISH
        }
    }

    fun stopGameManually() {
        isTimerRunning = false
        currentState = GameState.FINISH
    }

    fun backToMenu(vibrator: Vibrator?) {
        vibrator?.cancel()
        isTimerRunning = false
        currentState = GameState.SETTINGS
    }
}