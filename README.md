# CustomerAppointments - C950 WGU
Customer Appointments Assignment - C950 WGU

C950 Project - WGU

Customers and appointments are linked together via foreign keys in a MySQL database.  Best practices were followed when communicating with the database utilizing prepared statements, and there were requirements to utilize joins on multiple tables, as well as follow foreign key constraints. Program offers functionality to add, update, or remove customers and appointments.  Furthermore, login validation is required.  Notification of appointments within 15 minutes of program start is also implemented and displayed in a dialog box.

Also utilizes [User Time Zone]->EST conversions in order to populate time selector due to business operating hours constraints.  Therefore, it is impossible for a user to schedule appointments outside of business hours.  Checks time against database to ensure new apopintments or modified appointments fit constraints.  Includes error checking and form validation.

When an appointment or customer is modified or created, modification occurs in the instantiated class object in addition to the MySQL database.

Overall, this course required higher-level development practices as well as advanced Java knowledge and many other constraints, causing this to be a challenging project to complete as an undergraduate student.
