/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2015-03-26 20:30:19 UTC)
 * on 2015-07-01 at 17:09:28 UTC 
 * Modify at your own risk.
 */

package com.eduattendanceapp.studentendpoint.model;

/**
 * Model definition for Student.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the studentendpoint. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Student extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer admissionNumber;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String className;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String dob;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String firstName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String gender;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String lastName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer rollNumber;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String schoolName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String studentId;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getAdmissionNumber() {
    return admissionNumber;
  }

  /**
   * @param admissionNumber admissionNumber or {@code null} for none
   */
  public Student setAdmissionNumber(java.lang.Integer admissionNumber) {
    this.admissionNumber = admissionNumber;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getClassName() {
    return className;
  }

  /**
   * @param className className or {@code null} for none
   */
  public Student setClassName(java.lang.String className) {
    this.className = className;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDob() {
    return dob;
  }

  /**
   * @param dob dob or {@code null} for none
   */
  public Student setDob(java.lang.String dob) {
    this.dob = dob;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName firstName or {@code null} for none
   */
  public Student setFirstName(java.lang.String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getGender() {
    return gender;
  }

  /**
   * @param gender gender or {@code null} for none
   */
  public Student setGender(java.lang.String gender) {
    this.gender = gender;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLastName() {
    return lastName;
  }

  /**
   * @param lastName lastName or {@code null} for none
   */
  public Student setLastName(java.lang.String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getRollNumber() {
    return rollNumber;
  }

  /**
   * @param rollNumber rollNumber or {@code null} for none
   */
  public Student setRollNumber(java.lang.Integer rollNumber) {
    this.rollNumber = rollNumber;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getSchoolName() {
    return schoolName;
  }

  /**
   * @param schoolName schoolName or {@code null} for none
   */
  public Student setSchoolName(java.lang.String schoolName) {
    this.schoolName = schoolName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getStudentId() {
    return studentId;
  }

  /**
   * @param studentId studentId or {@code null} for none
   */
  public Student setStudentId(java.lang.String studentId) {
    this.studentId = studentId;
    return this;
  }

  @Override
  public Student set(String fieldName, Object value) {
    return (Student) super.set(fieldName, value);
  }

  @Override
  public Student clone() {
    return (Student) super.clone();
  }

}
