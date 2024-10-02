# Financial Bank

A bank will have customers/clients who will open a bank account with them and give the bank for safe keeping or for other
business operations. You can consider these customers as account holder, and the bank account as a store of value and its utilisation
on behalf of the customer.

The account holder will expect to carry some operations on this account.
Some of these operations include withdrawal, depositing or transferring e.t.c.

Money is a sensitive object and the owner (bank customer) will want to access and use it any given time. The custodian of this money on behalf of the
owner will need be trustworthy and ensure availability and security of this value. The customer will expect correctness and transparency in managing the value
being held by the bank.

Therefore, the bank will need to keep an accurate log of operations (transactions) happening on this account. All transactions
that are considered as withdrawal must be appear in this log and be logical. This log is supposed to show how the value being held
by the bank is depleted or nourished.

The customer will expect to periodically receive/query this transactions log and check their account balance.

### Important Considerations

1. Strong consistency, auditability, correctness
    
   - The customer wishes to have accurate logs of the account operations and correct balances at any given time.
   They don't want to have duplicate or missing entries in the logs. Therefore, I will need to have a transactional
   database with ACID properties to ensure strong consistency.
   
   - Eventual consistency shouldn't be considered when doing operations on the account. This is the reasons why in my code
   I have a change of value and logging happens in a database transaction which will guarantee strong consistency.

   - Most distributed systems or ledgers uses some sort of a ordered log of events to ensure data consistency. A good example is
   how [Apache Kafka's log implementation](https://kafka.apache.org/documentation/#log) whic we may use for transaction logging, causality resolution and eventing to downstream service.
   The logging technique is something that [Restate](https://restate.dev/blog/why-we-built-restate/) has also considered. My naive implementation of such a log
   is in using relational DB table for append only immutable transactions.

   - I may choose to have my transaction logs in database like AWS DynamoDB that uses [2PC](https://en.wikipedia.org/wiki/Two-phase_commit_protocol) to ensure consistency.

   - Furthermore, the `withdraw` API has been made idempotent by using the request parameters in generating a hash that can be
   used as a primary key in the transaction log.
2. Fault tolerance
   - Atomic writes on the transaction log will ensure I only one account writer and this will only succeed if there are no
   interruptions. In case, the service fails after the write succeeded, then subsequent retries will succeed but will return
   a copy of the previous operation before failure.
3. Scalability
   - Since I am using a relational DB, I am going to reduce querying it by first accessing a cache cluster to retrieve/lookup
   a transaction. There are many strategies to make the database scalable such as sharding, federation, replication, indexing,
   denormalization and query optimisations which I will not discuss here.
   - This application can also be scaled horizontally and put in front of a loadbalancer.
4. maintainability, structure, dependency management, portability
   - In adopting [DDD](https://martinfowler.com/bliki/DomainDrivenDesign.html), we have created domain entities and domain logic
   that operates on their own without any dependencies. This makes it easy for the business logic to be ported easily.
   - To even make this more possible, I have adopted [clean architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
   to achieve portability and maintenability of the code.
   - Our business logic is seperated from our choice of web framework, eventing strategy, databases and caching service.
   Which means, we can use our business logic with another JVM web framework other Java Spring boot, Redis instead of in-memory
   cache, Postgres instead of Mysql or DynamoDB instead of Cassandra, Apache Kafka instead of AWS SNS.
   - I have made sure that I adhere to [SRP](https://en.wikipedia.org/wiki/Single-responsibility_principle) principle and that is
   why eventing, persistence, withdrawal operations have been seperated.
   I have also added service based vector clocks and event timestamps to be used by the downstream services. Every event published will
   have vector clock and timestamps.



# References
1. [The Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html), 2012 - Robert C. Martin
2. [Domain Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html), 2020 - Martin Fowler
3. [Apache Kafka's log implementation](https://kafka.apache.org/documentation/#log)
4. [Restate](https://restate.dev/blog/why-we-built-restate/)
5. [2PC](https://en.wikipedia.org/wiki/Two-phase_commit_protocol)
6. [SRP](https://en.wikipedia.org/wiki/Single-responsibility_principle)