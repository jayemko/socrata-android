Unofficial Socrata android Java API
====================

Unoffcial android java api of socrata/socrata-api-java
Develop to work on android dispositive.
features:
	all View management methods
	- 'get' method
	- 'post' method for querying dataset
	- put and another method are not implemented yet

EXAMPLES
=================================

Creating a dataset
------------------

    Connection c = new HttpConnection("robots.dod.gov", "sam.gibson@socrata.com", "******", "******");
    View v = new View();
    v.setName("Robotic Attack Units by Year");
    v.setDescription("Seriously? You need more of a description than that?");
    View.Column unit = new View.Column();
    unit.setName("Unit");
    unit.setDataTypeName("text");

    View.Column year = new View.Column();
    year.setName("Year");
    year.setDataTypeName("number");

    v.addColumn(unit);
    v.addColumn(year);

    v.create(c);


Retrieving a single row
-----------------------

    Connection c = new HttpConnection("facilities.doe.gov", "clint.tseng@socrata.com", "******", "******");
    View v = View.find("faci-ltes", conn);
    View.Row r = v.getRow(1);

    // let's see what's in the first column
    Column col = v.getColumns().get(0);
    Object value = r.getDataField(col);

Retrieving multiple rows
------------------------

    Connection c = new HttpConnection("facilities.doe.gov", "clint.tseng@socrata.com", "******", "******");
    View v = View.find("faci-ltes", conn);

    // get the first 200 rows
    List<View.Row> r = v.getRows(0, 200, c);

Creating a row
--------------

    Connection c = new HttpConnection("facilities.doe.gov", "clint.tseng@socrata.com", "******", "******");
    View v = View.find("faci-ltes", conn);

    Column col = v.getColumnById(128583);

    View.Row r = new View.Row();
    r.putDataField(col, "Test value");
    v.appendRow(r);

Updating or deleting a row
--------------------------

    Connection c = new HttpConnection("facilities.doe.gov", "clint.tseng@socrata.com", "******", "******");
    View v = View.find("faci-ltes", conn);
    View.Row r = v.getRow(1);

    r.putDataField(v.getColumns().get(0), 3.14159);
    r.update(c);

    r.delete(c);

