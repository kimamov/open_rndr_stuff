import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.FontImageMap
import org.openrndr.draw.loadImage
import org.openrndr.draw.shadeStyle
import org.openrndr.draw.tint
import org.openrndr.math.Vector2

fun main() = application {
    var ballArray = arrayOf(70.0,80.0)
    var ballPos: Vector2=Vector2(180.0,240.0)

    configure {
        width = 768
        height = 576
    }

    program {
        extend {
            drawer.shadeStyle= shadeStyle { fragmentTransform="""
                
            """.trimIndent()
            parameter("", ballPos)
            parameter("arrLength", ballArray.size)

            }
        }
    }
}

