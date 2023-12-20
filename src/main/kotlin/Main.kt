import java.util.*

//Lab 8

fun main() {
    var P = 0
    while (P == 0)
        P = inputP()
    var Q = 0
    while (Q == 0)
        Q = inputQ()
    val N = P * Q
    val FI = (P - 1) * (Q - 1)
    var K_Open = 0
    var K_Close = 0
    var NeRavn = false
    while (!NeRavn) {
        while (K_Open == 0)
            K_Open = RAND(FI)
        K_Close = evklid(K_Open, FI)
        if (K_Open != K_Close) {
            NeRavn = true
        } else K_Open = 0
    }
    println("K_Open = $K_Open")
    println("K_Close = $K_Close")
    println("Пересылаем пользователю А пару чисел (N = $N, К_Open= $K_Open)")
    val strk = "*АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
    val mas = IntArray(100000)
    println("Пользователь А вводит текст:")
    val vvod: String = readln()
    var m = 0
    for (i in 0..vvod.length - 1) {
        for (j in 0..strk.length - 1) {
            if (vvod[i] == strk[j]) {
                mas[i] = gorner(j, K_Open, N)!!
                m = (m xor mas[i]).mod(N)
            }
        }
    }
    println("m=$m")
    var k = 0
    while (k == 0)
        k = rand(N)
    val obratnK = evklid(k, N) //пункт 7
    println("k=$k,obratnK=$obratnK")
    val m1 = (m * gorner(k, K_Open, N)!!).mod(N)
    println("Сторона А отправляет m1=$m1 стороне B ")
    val s1 = gorner(m1, K_Close, N)
    println("Сторона B отправляет в ответ число s1=$s1")
    val s = (obratnK * s1!!).mod(N)
    println("s=$s")
    //val sss= s.mod(N)
    //val mmm= gorner(m,K_Close,N)       //s действительно является подписью m
    //println("sss=$sss /  mmm =$mmm")
}

fun evklid(a: Int, n: Int): Int {
    val u: Array<Int> = arrayOf(0, 1, n)
    val v: Array<Int> = arrayOf(1, 0, a)
    val t: Array<Int> = arrayOf(0, 0, 0)
    while (u[2] != 0 && u[2] != 1 && v[2] != 0) {
        val q = u[2] / v[2]
        for (i in 0..2) {
            t[i] = u[i] - v[i] * q
            u[i] = v[i]
            v[i] = t[i]
        }
    }
    return if (u[2] != 1) {
        0
    } else {
        val uLit = u[0]
        if (uLit < 0) {
            val lit = uLit + n
            lit
        } else {
            uLit
        }
    }
} //нахожу закрытый ключ через расширенный алг евклида

fun simpleEvklid(FI: Int, K_Open: Int): Boolean {
    var num1 = FI
    var num2 = K_Open
    while (num1 != num2) {
        if (num1 > num2)
            num1 -= num2
        else
            num2 -= num1
    }
    return if (num1 == 1) {
        true
    } else {
        false
    }
} //АЛГОРИТМ ЕВКЛИДА: ЕСЛИ НОД = 1 , ТО TRUE

fun rand(N: Int): Int {
    val k = Random().nextInt(1, N - 1)
    return if (simpleEvklid(N, k) == false)
        0
    else k
}

fun RAND(FI: Int): Int {
    val random = Random()
    val K_Open = random.nextInt(2, FI)
    return if (simpleEvklid(FI, K_Open) == false)
        0
    else K_Open
}

fun inputP(): Int {
    var P: Int = 0
    println("Введите простое число P:")
    try {
        P = readln().toInt()
        var flag = true
        for (i in 2..(P / 2)) {
            if (P % i == 0) {
                flag = false
                break
            }
        }
        return if (P < 1 || !flag) {
            println("Некорректный ввод. Попробуйте ещё раз")
            0
        } else P
    } catch (e: NumberFormatException) {
        println("Некорректный ввод. Попробуйте ещё раз")
        return 0
    }
}  //ВВОДИМ ПРОСТОЕ ЧИСЛО P

fun inputQ(): Int {
    var Q: Int = 0
    println("Введите простое число Q:")
    try {
        Q = readln().toInt()
        var flag = true
        for (i in 2..(Q / 2)) {
            if (Q % i == 0) {
                flag = false
                break
            }
        }
        return if (Q < 1 || !flag) {
            println("Некорректный ввод. Попробуйте ещё раз")
            0
        } else Q
    } catch (e: NumberFormatException) {
        println("Некорректный ввод. Попробуйте ещё раз")
        return 0
    }
}  //ВВОДИМ ПРОСТОЕ ЧИСЛО Q

fun gorner(a: Int, x: Int, m: Int): Int? {
    var r = 1
    var k = 0
    var y: Int? = null
    if (m == 0) return null
    if (a == 0) return 0
    if (a == 1 || x == 0) return 1
    while (r <= x && k < 32) {
        r = (1 shl k)
        k++
    }
    k--
    if (k == 0 || k > 32) return null
    r = a
    y = if (x % 2 == 1) a
    else 1
    for (i in 1..k - 1) {
        r = (r * r).mod(m)
        if (((x shr i) and 1) == 1) {
            if (y != null) {
                y = (y.toInt() * r).mod(m)
            }
        }
    }
    return y
} //a^x mod m