/*
 * Packages
 *
 * the package keyword declares that all classes in the file are included in
 * the namespace of the package.
 *
 * The naming convention is domain.organization.packagename.
 * i.e.: com.mobpro.utils
 * the binary output of the compiler will store the .class files in a structure
 * that matches the source structure of the package.
 *
 * Aliased imports are done as follows:
 * import collection.mutable.{Map=>MutableMap}
 *
 * Packages can also be defined with curly braces which delimit the namespace of
 * that package. See example A.
 *
 */
package smallScope {
  class A
  abstract class B
}
