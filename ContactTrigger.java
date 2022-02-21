trigger ContactTrigger on Contact (before Insert,After Update){ 
Set<Id> accountIds = new Set<Id>(); 
Set<Id> contactIds = new Set<Id>(); 
Map<String, Integer> accountwithContactCount = new Map<String, Integer>();
List<Contact> oldContactList = new list<Contact>(); 
Integer Count =0;
if(trigger.IsInsert && trigger.IsBefore){
    for (Contact thisContact : trigger.new){
        if(newContact.Primary__c == true && newcontact.AccountId!=NULL ){
            if(!accountIds.Contains(newContact.AccountId)){            
                accountIds.add(newcontact.AccountId);                    
            }else{                   
                thisContact.addError('Only one Primary Contact Allow per Account');
            }                
        }
    }    

    if((accountIds !=NULL) && (accountIds.size()>0)){        
        oldContactList =  [SELECT Name,Primary__c,AccountId 
                            FROM Contact 
                            WHERE AccountId IN :accountIds AND Primary__c=TRUE];
        for(Contact cnt :OldContactList){                                                 
            accountwithContactCount.put(cnt.AccountId,1);                                                                                         
        }
        for(Contact thisContact : trigger.new){
            if(thisContact.AccountId!=NULL && accountwithContactCount.containsKey(thisContact.AccountId)){
              Count = accountwithContactCount.get(thisContact.AccountId);
              if(Count==1){                   
                  thisContact.addError(Only one Primary Contact Allow per Account');
              }
            } 
        }
    }        
}

if(trigger.IsUpdate && trigger.IsAfter){
    for(Contact thisContact : Trigger.new){

        if(thisContact.Primary__c == true && thisContact.AccountId!=NULL &&
         (Trigger.oldmap.get(thisContact.Id).Primary__c == FALSE || 
            Trigger.oldmap.get(thisContact.Id).Primary__c == TRUE )){ 

                if(!accountIds.Contains(thisContact.AccountId)){
                    accountIds.add(thisContact.AccountId);                                                                                                            
                    contactIds.add(newupContact.Id);
                }else if(accountIds.Contains(newupContact.AccountId)){          
                    thisContact.addError('When updating or inserting, Only 1 contact can be the primary. uncheck all other Primary custom field');
                }
        }                                              
    }   
    if((accountIds !=NULL) && (accountIds.size()>0))
    {    
        OldContactList=    [SELECT Id,Name,Primary__c,AccountId 
                            FROM Contact 
                            WHERE AccountId IN :accountIds AND Primary__c=TRUE AND Id NOT IN :contactIds];               
        for(Contact thisContact : OldContactList){                                
            accountwithContactCount.put(thisContact.AccountId,1);  
        }
        for(Contact thisContact : trigger.new){

            if(thisContact.AccountId!=NULL && accountwithContactCount.containsKey(thisContact.AccountId)){
            Count = accountwithContactCount.get(thisContact.AccountId);                                
                if((Count==1)&&(thisContact.Primary__c==TRUE)){
                  thisContact.addError('update is not Possible. one primary contact is allowed for a Account and it is already Available');
                }
            }

        }            
    }        
}
}
