import org.openrndr.Fullscreen
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.math.Vector2
import kotlin.random.Random

fun randomReverse(input: Double, from: Double, until: Double): Double {
    return if (input >= 0.0) Random.nextDouble(from = from, until = until) * -1 else Random.nextDouble(
        from = from,
        until = until
    )
}

class Ball(pos: Vector2, glowRadius: Double, movement: Vector2) {
    var pos: Vector2 = Vector2(0.0, 0.0)
    var glowRadius: Double = 10.0
    var movement: Vector2
    var colorShift: Vector2
    var lastCollision: String

    init {
        this.pos = pos
        this.glowRadius = glowRadius
        this.movement = movement
        this.colorShift = Vector2(Random.nextDouble(until = 1.0), Random.nextDouble(until = 1.0))
        this.lastCollision = ""
    }

    fun updatePos() {
        this.pos += this.movement
    }

    fun collisionEvent(top: Double, right: Double, bottom: Double, left: Double) {
        var rememberCollision: String = this.lastCollision
        if (this.pos.y < top) {
            if (lastCollision != "top") {
                this.lastCollision = "top"
                this.movement = Vector2(this.movement.x, randomReverse(this.movement.y, 4.0, 14.0))

            }

        }
        if (this.pos.x > right) {
            if (this.lastCollision != "right") {
                this.lastCollision = "right"
                this.movement = Vector2(randomReverse(this.movement.x, 4.0, 14.0), this.movement.y)

            }
        }
        if (this.pos.y > bottom) {
            if (lastCollision != "bottom") {
                this.lastCollision = "bottom"
                this.movement = Vector2(this.movement.x, randomReverse(this.movement.y, 4.0, 14.0))

            }
        }
        if (this.pos.x < left) {
            if (this.lastCollision != "left") {
                this.lastCollision = "left"
                this.movement = Vector2(randomReverse(this.movement.x, 4.0, 14.0), this.movement.y)

            }

        }
        if (rememberCollision != this.lastCollision) {
            this.colorShift = Vector2(Random.nextDouble(until = 1.0), Random.nextDouble(until = 1.0))
            this.glowRadius = Random.nextDouble(from = 4.0, until = 20.0)
        }
    }

}


fun main() = application {
    //var newBall=Ball(160.0, 280.0, 20.0)

    var ballX = 460.0;
    var ballY = 640.0;
    var ballMoveX =/*Random.nextDouble(until=10.0)-10.0*/ 10.0;
    var ballMoveY =/*Random.nextDouble(until=10.0)-10.0*/ 8.0;
    var currentCol = Vector2(Random.nextDouble(until = 1.0), Random.nextDouble(until = 1.0))
    var mySize: Double = 20.0
    var ballArr: ArrayList<Ball>
    configure {
        width = 1080
        height = 1080
        //fullscreen=Fullscreen.SET_DISPLAY_MODE
        //randomX=Random.nextFloat(until = width.toFloat())
        //randomY=Random.nextFloat(until = height.toFloat())
        //randomR=Random.nextDouble(until = 100.0);
    }

    program {
        /*CREATE SOME BALLS*/
        var ballList: MutableList<Ball> = ArrayList()
        for (i in 0..3) {
            ballList.add(
                Ball(
                    Vector2(
                        Random.nextDouble(until = width.toDouble()),
                        Random.nextDouble(until = height.toDouble())
                    ),
                    Random.nextDouble(until = 10.0),
                    Vector2(Random.nextDouble(until = 10.0), Random.nextDouble(until = 10.0))
                )
            )
        }

        extend {
            drawer.shadeStyle = shadeStyle {
                fragmentTransform = """
                |vec2 normScreenPos=c_screenPosition/p_resolution;
                |
                |
                |vec2 normBallPos=p_ballPos/p_resolution;
                |float ballDistance=distance(normBallPos, normScreenPos);
                |float multiplyDistance=ballDistance*100.0;
                |vec2 sumColor=vec2(p_size/multiplyDistance)*vec2(0.7,0.9);
                |
                |normBallPos=p_ballPosTwo/p_resolution;
                |ballDistance=distance(normBallPos, normScreenPos);
                |multiplyDistance=ballDistance*100.0;
                sumColor+=vec2(p_sizeTwo/multiplyDistance)*vec2(0.9,0.3);

                |
                normBallPos=p_ballPosThree/p_resolution;
                |ballDistance=distance(normBallPos, normScreenPos);
                |multiplyDistance=ballDistance*100.0;
                |sumColor+=vec2(p_sizeThree/multiplyDistance)*vec2(0.5,0.7);

                |
                |
                x_fill.rgba=vec4(vec3(sumColor, 1.0),1.0);

                |//x_fill.rgba=vec4(vec3(mod(sumColor,2.0),mod(sumColor,1.0),mod(sumColor,3.0)),1.0);
            """.trimMargin()
                parameter("resolution", Vector2(width.toDouble(), height.toDouble()))
                parameter("colVec", ballList[0].colorShift)

                parameter("ballPos", ballList[0].pos)
                parameter("size", ballList[0].glowRadius)
                parameter("ballPosTwo", ballList[1].pos)
                parameter("sizeTwo", ballList[1].glowRadius)
                parameter("ballPosThree", ballList[2].pos)
                parameter("sizeThree", ballList[2].glowRadius)

            }
            drawer.fill = ColorRGBa.PINK
            drawer.rectangle(0.0, 0.0, width.toDouble(), height.toDouble())
            for (ball in ballList) {
                ball.collisionEvent(0.0, width.toDouble(), height.toDouble(), 0.0)
                ball.updatePos()
            }
        }
    }


}

