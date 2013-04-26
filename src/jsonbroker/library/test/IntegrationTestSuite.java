package jsonbroker.library.test;

import jsonbroker.library.service.test.TestIntegrationTest;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@IncludeCategory(IntegrationTest.class)
@SuiteClasses(TestIntegrationTest.class)
public class IntegrationTestSuite {

}
