import org.openrndr.Fullscreen
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.math.Vector2
import kotlin.random.Random

class Ball(x: Double,y:Double, glowRadius: Double, movement : Vector2){
    var x : Double=0.0
    var y : Double=0.0
    var glowRadius : Double=10.0
    var movement : Vector2
    init {
        this.x=x
        this.y=y
        this.glowRadius=glowRadius
        this.movement=movement
    }
    fun updatePos(moveX: Double, moveY: Double){
        this.x+=moveX
        this.y+=moveY
    }

}

fun checkCol(x: Double, y: Double, moveX: Double, moveY: Double, w: Int, h: Int, cX: Double, cY: Double, size: Double) : Array<Double>{
    var moveXOut : Double=moveX;
    var moveYOut : Double=moveY;
    var colX : Double=cX
    var colY : Double=cY
    var mySize : Double=size
    if(x>w.toDouble() || x<0.0){
        moveXOut*=-1.0;
        colX=Random.nextDouble(until=1.0)
        mySize=Random.nextDouble(from=2.0 ,until=40.0)
    }
    if(y>h.toDouble() || y<0.0){
        moveYOut*=-1.0;
        colY=Random.nextDouble(until=1.0)
        mySize=Random.nextDouble(from=2.0 ,until=40.0)

    }

    //currentCol=Vector2(Random.nextDouble(until=1.0),Random.nextDouble(until=1.0))
    return arrayOf(moveXOut, moveYOut, colX,colY, mySize)

}

fun main() = application {
    //var newBall=Ball(160.0, 280.0, 20.0)

    var ballX=460.0;
    var ballY=640.0;
    var ballMoveX=/*Random.nextDouble(until=10.0)-10.0*/ 10.0;
    var ballMoveY=/*Random.nextDouble(until=10.0)-10.0*/ 8.0;
    var currentCol=Vector2(Random.nextDouble(until=1.0),Random.nextDouble(until=1.0))
    var mySize : Double= 20.0

    configure {
        width = 1080
        height = 1080
        //fullscreen=Fullscreen.SET_DISPLAY_MODE
        //randomX=Random.nextFloat(until = width.toFloat())
        //randomY=Random.nextFloat(until = height.toFloat())
        //randomR=Random.nextDouble(until = 100.0);
    }

    program {
        extend{
            //for(x in 0..10){
            //    drawer.circle(x=Random.nextDouble(until = width.toDouble()), y=Random.nextDouble(until = height.toDouble()), radius= Random.nextDouble(until = 160.0));
            //}
            val newMove=checkCol(ballX,ballY,ballMoveX,ballMoveY,width,height,currentCol.x,currentCol.y,mySize)

            ballMoveX=newMove[0]
            ballMoveY=newMove[1]
            currentCol=Vector2(newMove[2],newMove[3])
            mySize=newMove[4]
            ballX+=ballMoveX;
            ballY+=ballMoveY;




            drawer.shadeStyle= shadeStyle {  fragmentTransform="""
                |float multiply=p_size;
                |vec2 normBallPos=p_ballPos/p_resolution;
                |vec2 normScreenPos=c_screenPosition/p_resolution;
                |float ballDistance=distance(normBallPos, normScreenPos);
                |
                |float multiplyDistance=ballDistance*multiply;
                |
                |x_fill.rgba=vec4(vec3((1.0/multiplyDistance)*vec3(p_colVec,1.0)),1.0);
            """.trimMargin()
                parameter("resolution", Vector2(width.toDouble(),height.toDouble()))
                parameter("ballPos", Vector2(ballX,ballY))
                parameter("colVec",currentCol)
                parameter("size",mySize)
                //parameter("ballPosTwo", Vector2(ballX,ballY))
                //parameter("colVec",currentCol)
                //parameter("sizeTwo",mySize)
            }
            drawer.fill= ColorRGBa.PINK
            drawer.rectangle(0.0,0.0,width.toDouble(),height.toDouble())
        }
    }


}