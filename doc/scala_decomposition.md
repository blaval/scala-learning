# Learning Scala decomposition

## Table of content

1. [How to work with custom unapply](#how-to-work-with-custom-unapply)
2. [How to work with custom unapplySeq](#how-to-work-with-custom-unapplyseq)
3. [How to extract property and infix pattern](#how-to-extract-property-and-infix-pattern)

## How to work with custom unapply

```scala
case class Server(consulServiceName: Option[String], k8sServiceName: Option[String], istioEnabled: Option[Boolean])


trait WhateverName[T] {
  def isEmpty: Boolean

  def get: T
}

object ConsulServer {
  type ConsulServiceName = String

  def unapply(server: Server): WhateverName[ConsulServiceName] = new WhateverName[ConsulServiceName] {

    // when true throw scala.MatchError exception
    override def isEmpty = server match {
      case Server(Some(consulServiceName), None, None) => false
      case _ => true
    }

    override def get: ConsulServiceName = server.consulServiceName.getOrElse(
      throw new RuntimeException("isEmpty method is not correctly implemented as consulServiceName cannot be retrieved")
    )
  }

}

object K8sServer {
  type ServiceName = String
  type IstioEnabled = Option[Boolean]

  def unapply(server: Server): Option[(ServiceName, IstioEnabled)] = server match {
    case Server(None, Some(k8sServiceName), istioEnabled) => Some(k8sServiceName -> istioEnabled)
    case _ => None
  }

}

object InvalidServer {
  // just an example of unapply working with no parameter in the decomposition
  def unapply(server: Server): Boolean = true

}

//val consulServiceNameOpt = None
val consulServiceNameOpt = Some("consul.service.name")
val k8sServiceNameOpt = None
//val k8sServiceNameOpt = Some("k8s.service.name")
val istioEnabledOpt = None
//val istioEnabledOpt = Some(true)
val server = Server(consulServiceNameOpt, k8sServiceNameOpt, istioEnabledOpt)

server match {
  // instead of case Server(None, Some(k8sServiceName), istioEnabled) =>
  case K8sServer(k8sServiceName, istioEnabled) =>
    s"K8S server ======> $k8sServiceName $istioEnabled"
  // instead of case Server(Some(consulServiceName), None, None) =>
  case ConsulServer(consulServiceName) =>
    s"CONSUL server ======> $consulServiceName"
  case InvalidServer() =>
    s"I AM NOT A VALID SERVER"
}
```

## How to work with custom unapplySeq

```scala
case class Server(consulServiceName: Option[String], k8sServiceName: Option[String], istioEnabled: Option[Boolean])


trait WhateverName[T] {
  def isEmpty: Boolean

  def get: T
}

object ConsulServer {
  type ConsulServiceName = String

  def unapply(server: Server): Option[ConsulServiceName] = server match {
    case Server(Some(consulServiceName), None, None) => Some(consulServiceName)
    case _ => None
  }

}

object K8sServer {
  type ServiceName = String
  type IstioEnabled = Option[Boolean]

  def unapply(server: Server): Option[(ServiceName, IstioEnabled)] = server match {
    case Server(None, Some(k8sServiceName), istioEnabled) => Some(k8sServiceName -> istioEnabled)
    case _ => None
  }

}

object InvalidServer {
  def unapply(server: Server): Boolean = true
}

object Servers {
  // if unapply and unapplySeq are in the same scope unapplySeq will be ignored
  // def unapply(servers: Seq[Server]): Option[Servers] = ???

  def unapplySeq(servers: Seq[Server]): Option[Seq[Server]] =
    if (servers.isEmpty) None else Some(servers)
}


//val consulServiceNameOpt = None
val consulServiceNameOpt = Some("consul.service.name")
val k8sServiceNameOpt = None
//val k8sServiceNameOpt = Some("k8s.service.name")
val istioEnabledOpt = None
//val istioEnabledOpt = Some(true)
val server = Server(consulServiceNameOpt, k8sServiceNameOpt, istioEnabledOpt)
//val servers = Seq(server)
//val servers = Seq(server, Server(Some("anotherconsul.service.name"), None, None))
//val servers = Seq(server, server)
val servers = Seq(server, server, server)

servers match {
  case s@Servers(k8sRef@K8sServer(k8sServiceName, istioEnabled), consulRef@ConsulServer(consulServiceName)) =>
    s"A list of 2 servers (length=${s.length}) CONSUL($consulServiceName) => " +
      s"$k8sRef -> $consulRef: K8S($k8sServiceName, $istioEnabled) and CONSUL($consulServiceName) => $k8sRef -> $consulRef"
  case s@Servers(ConsulServer(consulServiceName), _*) =>
    s"A list of at least 1 consul server (length=${s.length}):  CONSUL($consulServiceName)"
  case _ =>
    s"I AM NOT A VALID SERVER"
}
```

## How to extract property and infix pattern

```scala
case class Server(consulServiceName: Option[String], k8sServiceName: Option[String], istioEnabled: Option[Boolean])

object ConsulServer {
  type ConsulServiceName = String

  def unapply(server: Server): Option[ConsulServiceName] =
    server.consulServiceName
}

// val server = Server(None, Some("k8s.service.name"), None)
val server = Server(Some("consul.service.name"), None, None)
// If the matching is not possible it will throw a matchError exception
val ConsulServer(consulServiceName) = server

// Example of infix pattern
case class Or(serverA: Server, serverB: Server)

val Or(serverA, serverB) = Or(server, server)
val serverA Or serverB = Or(server, server)
val serverA Or ConsulServer(consulServiceName) = Or(server, server)
val serverA Or ConsulServer(consulServiceName) = Or(server, Server(None, None, None))

// How to solve the matchError exception
object ConsulServerWithoutMatchError {
  type ConsulServiceName = String

  def unapply(server: Server): Option[ConsulServiceName] = Some(
    server.consulServiceName.getOrElse("default.service.name")
  )
}

val serverA Or ConsulServerWithoutMatchError(consulServiceName) = Or(server, Server(None, None, None))

// Example with reference
val orRef@serverA Or (serverBRef@ConsulServerWithoutMatchError(consulServiceName)) = Or(server, Server(None, None, None))

// Examples of decomposition with reference and unapplySeq
val Seq(serverA, serverB, _*) = Seq(1, 2, 3)
val serverA :: serverB :: _ = Seq(1, 2, 3)
val seqRef@Seq(serverA, serverBRef@serverB, _*) = Seq(1, 2, 3)
val seqRef(serverA :: (serverBRef@serverB) :: _ = Seq(1, 2, 3)
```
