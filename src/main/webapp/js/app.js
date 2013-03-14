// app.js
(function($){

		var LocalJobs = {};
		window.LocalJobs = LocalJobs;
		
		var template = function(name) {
			return Mustache.compile($('#'+name+'-template').html());
		};
		
		LocalJobs.HomeView = Backbone.View.extend({
			tagName : "form",
			el : $("#main"),
			
			events : {
				"submit" : "findJobs"
			},
			
			render : function(){
				return this;
			},
			
			findJobs : function(event){
				console.log('in findJobs()...');
				event.preventDefault();
				$("#results").empty();
				$("#jobSearchForm").mask("Finding Jobs ...");
				var skills = this.$('input[name=skills]').val().split(',');
				var location = this.$("#location").val();
				var company = this.$("#company").val();
				var strategy = this.$("#strategy").val();
				
				console.log("skills : "+skills);
				console.log("location : "+location);
				console.log("company : "+company);
				console.log("strategy : "+strategy);
				
				var self = this;
				$.get("api/jobs/"+location+"/"+skills  , function (results){ 
                    console.log('Found data ... '+results);
                    $("#jobSearchForm").unmask();
                    self.renderResults(results,self);
                }); 
			},
			
			renderResults : function(results,self){
				_.each(results,function(result){
					self.renderJob(result);
				});
				
			},
			
			renderJob : function(result){
				console.log((result['content']));
				var jobView = new LocalJobs.JobView({result : result});
				$("#results").append(jobView.render().el);
			},
			
			

		});
		
		LocalJobs.JobView = Backbone.View.extend({
				template : template("job"),
				initialize  : function(options){
					this.result = options.result;
				},
		
				render : function(){
					this.$el.html(this.template(this));
					return this;
				},
				jobtitle : function(){
					console.log('in jobTitle : '+this.result);
					console.log((this.result['content'])['jobTitle']);
					return (this.result['content'])['jobTitle'];
				},
				address : function(){
					return (this.result['content'])['formattedAddress'];
				},
				skills : function(){
					return (this.result['content'])['skills'];
				},
				company : function(){
					return ((this.result['content'])['company'])['companyName'];
				},
				distance : function(){
					return (this.result['distance'])['value'] + " "+ (this.result['distance'])['metric'];
				}
				
				
				
		
		});
		
		
		LocalJobs.Router = Backbone.Router.extend({
			el : $("#main"),
			
			routes : {
				"" : "showHomePage"
			},
			showHomePage : function(){
				console.log('in home page...');
				var homeView = new LocalJobs.HomeView();
				this.el.append(homeView.render().el);
			}
		
		});
		
		var app = new LocalJobs.Router();
		Backbone.history.start();
		
		
})(jQuery);