package com.fdmgroup.BankAccountApp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BankAccountTest.class, ControllerTest.class, PersistenceTest.class })
public class AllTests {

}
