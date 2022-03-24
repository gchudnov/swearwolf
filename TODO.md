
object SttpBackendStub {

/** Create a stub of a synchronous backend (which doesn't use an effect type), without streaming.
*/
def synchronous: SttpBackendStub[Identity, WebSockets] =
new SttpBackendStub(
IdMonad,
PartialFunction.empty,
None
)

/** Create a stub of an asynchronous backend (which uses the Scala's built-in [[Future]] as the effect type), without
* streaming.
*/
def asynchronousFuture: SttpBackendStub[Future, WebSockets] = {
import scala.concurrent.ExecutionContext.Implicits.global
new SttpBackendStub(
new FutureMonad(),
PartialFunction.empty,
None
)
}