package org.amitayh.invoices.domain

import java.time.LocalDate
import java.util.UUID

case class Invoice(customer: Customer,
                   issueDate: LocalDate,
                   dueDate: LocalDate,
                   lineItems: Map[UUID, LineItem],
                   status: InvoiceStatus,
                   paid: Double) {

  def setCustomer(name: String, email: String): Invoice =
    copy(customer = Customer(name, email))

  def setDates(newIssueDate: LocalDate, newDueDate: LocalDate): Invoice =
    copy(issueDate = newIssueDate, dueDate = newDueDate)

  def addLineItem(lineItemId: UUID,
                  description: String,
                  quantity: Double,
                  price: Double): Invoice = {
    val lineItem = LineItem(description, quantity, price)
    copy(lineItems = lineItems + (lineItemId -> lineItem))
  }

  def removeLineItem(lineItemId: UUID): Invoice =
    copy(lineItems = lineItems - lineItemId)

  def pay(amount: Double): Invoice = {
    val newStatus = if (amount == balance) Paid else status
    copy(paid = paid + amount, status = newStatus)
  }

  def hasLineItem(lineItemId: UUID): Boolean =
    lineItems.contains(lineItemId)

  def total: Double = lineItems.values.foldLeft(0.0)(_ + _.total)

  def balance: Double = total - paid

}

object Invoice {
  val Draft = Invoice(
    customer = Customer.Empty,
    issueDate = LocalDate.MIN,
    dueDate = LocalDate.MAX,
    lineItems = Map.empty,
    status = New,
    paid = 0.0)
}

case class Customer(name: String, email: String)

object Customer {
  val Empty = Customer("", "")
}

case class LineItem(description: String, quantity: Double, price: Double) {
  def total: Double = quantity * price
}

sealed trait InvoiceStatus
case object New extends InvoiceStatus
case object Paid extends InvoiceStatus
