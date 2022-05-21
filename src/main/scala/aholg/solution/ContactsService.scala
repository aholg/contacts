package aholg.solution

import scala.concurrent.{ExecutionContext, Future}

class ContactsService(contactsRepository: ContactsRepository)(implicit ec: ExecutionContext) {

  def contacts(userId: UserId): Future[Seq[UserId]] = {
    contactsRepository.contacts(userId)
  }

  def updateContacts(userId: UserId, contacts: Seq[UserId]): Future[Unit] = {
    contactsRepository.updateContacts(userId, contacts)
  }

  def contactsOfContacts(userId: UserId): Future[Set[UserId]] = {
    for {
      userContacts <- contacts(userId)
      contactsOfContacts <- Future.sequence(userContacts.map(contacts)).map(_.flatten)
      deduplicatedContactsOfContacts = contactsOfContacts.filterNot(u => userContacts.contains(u)).toSet
    } yield deduplicatedContactsOfContacts
  }

}
