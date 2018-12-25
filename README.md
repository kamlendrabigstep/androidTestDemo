# Android Forms

This library is made for rendering dynamic android forms with rich attributes and many form's layouts.

# Implementation / Compile



# HOW TO USE?

@formResponse JSONObject,
@mContext Activity
FormWrapper formWrapper = new FormWrapper(mContext);
formWrapper.setFormSchema(formResponse);
View formLayout = formWrapper.getFormWrapper(mContext, formResponse);
Inject form layout in any view as per your requirement.

# Forms / Fields Response Format
  Generel Attributes Of Fields :- 
  name          ==>  Name of the filed [It must be unique in the form.]
  type          ==>  Field type Ex. text, select, checkbox etc.
  label         ==>  Label of the field which will be visible to user
  inputType     ==>  Specific attribute of TextField element which allowed type of input[number, phone, location etc]
  value         ==>  Value of the field
  required      ==>  To validated(Not allowed empty if true) the field at application level, it can be true or false. It become                      false by default.
  multioptions  ==>  Specific attribute of 
# Demo Screenshots Of Form Layouts/ Elements / UI


![alt text](https://raw.githubusercontent.com/kamlendrabigstep/androidTestDemo/master/screenshots/date_time_field_demo.gif)



