
�
google/protobuf/empty.protogoogle.protobuf"
EmptyB}
com.google.protobufB
EmptyProtoPZ.google.golang.org/protobuf/types/known/emptypb��GPB�Google.Protobuf.WellKnownTypesJ�
 2
�
 2� Protocol Buffers - Google's data interchange format
 Copyright 2008 Google Inc.  All rights reserved.
 https://developers.google.com/protocol-buffers/

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are
 met:

     * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
     * Redistributions in binary form must reproduce the above
 copyright notice, this list of conditions and the following disclaimer
 in the documentation and/or other materials provided with the
 distribution.
     * Neither the name of Google Inc. nor the names of its
 contributors may be used to endorse or promote products derived from
 this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


  

" E
	
" E

# ,
	
# ,

$ +
	
$ +

% "
	

% "

& !
	
$& !

' ;
	
%' ;

( 
	
( 
�
 2 � A generic empty message that you can re-use to avoid defining duplicated
 empty messages in your APIs. A typical example is to use it as the request
 or the response type of an API method. For instance:

     service Foo {
       rpc Bar(google.protobuf.Empty) returns (google.protobuf.Empty);
     }




 2bproto3
�
niffler-currency.protoguru.qa.grpc.nifflergoogle/protobuf/empty.proto"X
CurrencyResponseD
allCurrencies (2.guru.qa.grpc.niffler.CurrencyRallCurrencies"p
Currency@
currency (2$.guru.qa.grpc.niffler.CurrencyValuesRcurrency"
currencyRate (RcurrencyRate"�
CalculateRequestJ
spendCurrency (2$.guru.qa.grpc.niffler.CurrencyValuesRspendCurrencyN
desiredCurrency (2$.guru.qa.grpc.niffler.CurrencyValuesRdesiredCurrency
amount (Ramount"?
CalculateResponse*
calculatedAmount (RcalculatedAmount*E
CurrencyValues
UNSPECIFIED 
RUB
USD
EUR
KZT2�
NifflerCurrencyServiceT
GetAllCurrencies.google.protobuf.Empty&.guru.qa.grpc.niffler.CurrencyResponse" b
CalculateRate&.guru.qa.grpc.niffler.CalculateRequest'.guru.qa.grpc.niffler.CalculateResponse" B.
guru.qa.niffler.grpcBNifflerCurrencyProtoPJ�
  (

  
	
  %

 

 "
	

 "

 -
	
 -

 5
	
 5


 
 


 


  L

  

  -

  8H

 E

 

 %

 0A


  


 

  &

  


  

  !

  $%


 




 

 

 

 





	




 




 #

 

 

 !"

%



 

#$





	




  




 

 

 	

 


 " (


 "

  #

  #

  #

 $


 $

 $	

 %


 %

 %	

 &


 &

 &	

 '


 '

 '	bproto3