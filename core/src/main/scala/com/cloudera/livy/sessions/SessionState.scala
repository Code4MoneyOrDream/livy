/*
 * Licensed to Cloudera, Inc. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Cloudera, Inc. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.livy.sessions

sealed trait SessionState {
  /** Returns true if the State represents a process that can eventually execute commands */
  def isActive: Boolean
}

sealed trait FinishedSessionState extends SessionState {
  /** When session is finished. */
  def time: Long
}

object SessionState {

  def apply(s: String): SessionState = {
    s match {
      case "not_started" => NotStarted()
      case "starting" => Starting()
      case "recovering" => Recovering()
      case "idle" => Idle()
      case "running" => Running()
      case "busy" => Busy()
      case "shutting_down" => ShuttingDown()
      case "error" => Error()
      case "dead" => Dead()
      case "success" => Success()
      case _ => throw new IllegalArgumentException(s"Illegal session state: $s")
    }
  }

  case class NotStarted() extends SessionState {
    override def isActive: Boolean = true

    override def toString: String = "not_started"
  }

  case class Starting() extends SessionState {
    override def isActive: Boolean = true

    override def toString: String = "starting"
  }

  case class Recovering() extends SessionState {
    override def isActive: Boolean = true

    override def toString: String = "recovering"
  }

  case class Idle() extends SessionState {
    override def isActive: Boolean = true

    override def toString: String = "idle"
  }

  case class Running() extends SessionState {
    override def isActive: Boolean = true

    override def toString: String = "running"
  }

  case class Busy() extends SessionState {
    override def isActive: Boolean = true

    override def toString: String = "busy"
  }

  case class ShuttingDown() extends SessionState {
    override def isActive: Boolean = false

    override def toString: String = "shutting_down"
  }

  case class Error(time: Long = System.nanoTime()) extends FinishedSessionState {
    override def isActive: Boolean = true

    override def toString: String = "error"
  }

  case class Dead(time: Long = System.nanoTime()) extends FinishedSessionState {
    override def isActive: Boolean = false

    override def toString: String = "dead"
  }

  case class Success(time: Long = System.nanoTime()) extends FinishedSessionState {
    override def isActive: Boolean = false

    override def toString: String = "success"
  }
}
