import tsmodule {
    ...
}

void errlog(Object o) {
    dynamic {
        console.error(o);
    }
}

void assertEquals(Object expected, Object actual) {
    if (expected != actual) {
        errlog(expected);
        errlog(actual);
        throw AssertionError("expected != actual");
    }
}

void testConstEmptyString() {
    String es = constEmptyString;
    assertEquals { expected = ""; actual = es; };
    assertEquals { expected = ""; actual = constEmptyString; };
}

void testVarEmptyString() {
    String es = varEmptyString;
    assertEquals { expected = ""; actual = es; };
    assertEquals { expected = ""; actual = varEmptyString; };
    varEmptyString = "a";
    assertEquals { expected = "a"; actual = varEmptyString; };
    varEmptyString = "";
}

void testGetVarEmptyString() {
    String es = getVarEmptyString();
    assertEquals { expected = ""; actual = es; };
    assertEquals { expected = ""; actual = getVarEmptyString(); };
    varEmptyString = "a";
    assertEquals { expected = "a"; actual = getVarEmptyString(); };
    varEmptyString = "";
}

void testStringIdentity() {
    assertEquals { expected = ""; actual = stringIdentity(""); };
    assertEquals { expected = "foo"; actual = stringIdentity("foo"); };
    assertEquals { expected = "bar"; actual = stringIdentity { "bar"; }; };
    assertEquals { expected = "baz"; actual = stringIdentity { s = "baz"; }; };
}

void testNumberIdentity() {
    assertEquals { expected = 4.2; actual = numberIdentity(4.2); };
    assertEquals { expected = 1.0; actual = numberIdentity(1.0); };
    assertEquals { expected = 1; actual = numberIdentity(1.0); };
    assertEquals { expected = 1; actual = numberIdentity(1); };
}

void testBooleanIdentity() {
    assert (booleanIdentity(true));
    assert (!booleanIdentity(false));
}

suppressWarnings ("unusedDeclaration")
void testVoidFunction() {
    Anything a1 = voidFunction(1);
    Anything a2 = voidFunction("string");
    Anything a3 = voidFunction(testVoidFunction);
}

void testStringBox() {
    value sb = StringBox("s1");
    assertEquals { expected = "s1"; actual = sb.s; };
    sb.setS("s2");
    assertEquals { expected = "s2"; actual = sb.getS(); };
    sb.s = "s3";
    assertEquals { expected = "s3"; actual = sb.getS(); };
    sb.setS("s4");
    assertEquals { expected = "s4"; actual = sb.s; };
    StringBox sb2 = sb.self();
    assertEquals { expected = sb; actual = sb2; };
}

void testIStringBox() {
    IStringBox sb = makeIStringBox("s1");
    assertEquals { expected = "s1"; actual = sb.s; };
    sb.setS("s2");
    assertEquals { expected = "s2"; actual = sb.getS(); };
    sb.s = "s3";
    assertEquals { expected = "s3"; actual = sb.getS(); };
    sb.setS("s4");
    assertEquals { expected = "s4"; actual = sb.s; };
    IStringBox sb2 = sb.self();
    assertEquals { expected = sb; actual = sb2; };
}

shared void run() {
    testConstEmptyString();
    testVarEmptyString();
    testGetVarEmptyString();
    testStringIdentity();
    testNumberIdentity();
    testBooleanIdentity();
    testVoidFunction();
    testStringBox();
    testIStringBox();
}
