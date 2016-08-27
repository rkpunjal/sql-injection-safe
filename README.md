# Annotation for SQL Injection Safe Parameters and fields, and Util for Sql-Injection-Safe check

Through this library you get an annotation SQLInjectionSafe for validating if field-values or parameter-values are SQL-Injection safe

This Library has been thoroughly tested with various sql-injection-embedded data samples.

To use this, include this dependency in your pom.xml

        <dependency>
            <groupId>com.github.rkpunjal.sqlsafe</groupId>
            <artifactId>sql-injection-safe</artifactId>
            <version>1.0.0</version>
        </dependency>

A usage could look something like this
`private @SQLInjectionSafe String id;`

or you can also use the utility method
`SqlSafeUtil.isSqlInjectionSafe("my-string-value")`


**For a Detailed Explanation on how this library works, refer to my article here:**
[https://dzone.com/articles/custom-annotation-in-java-for-sql-injection-safe-p](https://dzone.com/articles/custom-annotation-in-java-for-sql-injection-safe-p)

It's the same code that has been slightly enhanced and modified so that now you have the annotation directly for your own use.

---

# Detail Explanation for usage in a Spring mvc controller

You can also this used it in a spring mvc controller to validate the incoming request parameter.

Spring MVC has it’s own way of running validators on RequestParameters.
Hence you have to first create a wrapper class annotate your parameter.

eg:

	public static class IdWrapper{
		private @SQLInjectionSafe String id;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	}


Then you can use the wrapper in your controller like this:


	@RequestMapping(value = "/getById)
	public MyResponseObject getById(
		@Valid @ModelAttribute() IdWrapper idWrapper){
			// do your stuff
	}



Now when you have a validation Failure on the incoming parameter, Spring throws a **BindException**.
If not handeled, the BindException is sent across as response directly.

To send a cleaner response, you can create an ExceptionHandler method in the controller.

This could be something like this :

	@ExceptionHandler(BindException.class)
	public @ResponseBody WebResponse handleBindException(BindException be ){
		return new MyResponseObject(false,
				getBindExceptionMessage(be) // custom method to find and send an appropriate response
		);
	}


the **getBindExceptionMessage** could look something like this

    public static final String INVALID_DATA_PROVIDED = "Invalid data provided";
    public static final String ID_WRAPPER = "idWrapper";

    protected String getBindExceptionMessage(BindException be){

        if(be==null && be.getBindingResult()==null){
            return INVALID_DATA_PROVIDED;
        }

        List<ObjectError> errors = be.getBindingResult().getAllErrors();

        if(errors==null || errors.isEmpty()){
            return INVALID_DATA_PROVIDED;
        }

        for(ObjectError objectError : errors){
            if(objectError instanceof FieldError){
                if(ID_WRAPPER.equalsIgnoreCase(objectError.getObjectName())){
                    return "Invalid 'id' specified";
                }
            }
        }

        return INVALID_DATA_PROVIDED;

    }

Refer to these links to understand in detail why you need to create a wrapper class for the parametes.
- [http://copyrightdev.tumblr.com/post/92560458673/tips-tricks-having-fun-with-spring-validators](http://copyrightdev.tumblr.com/post/92560458673/tips-tricks-having-fun-with-spring-validators)
- [http://stackoverflow.com/questions/6203740/spring-web-mvc-validate-individual-request-params](http://stackoverflow.com/questions/6203740/spring-web-mvc-validate-individual-request-params)


In case you have questions you can write to me.


**For a Detailed Explanation on how this library works, refer to my article here: **
[https://dzone.com/articles/custom-annotation-in-java-for-sql-injection-safe-p](https://dzone.com/articles/custom-annotation-in-java-for-sql-injection-safe-p)