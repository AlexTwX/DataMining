=======
DataMining
==========
    Creation d'un dataset de crashPython
    Creation d'un dataset de question avec leur reponse contenant un crashPython
    Creation d'un dataset de question avec leur reponse sans crashPython
    
Pourquoi avoir fait plusieur dataset ?
    La création d'un dataset présent prenant un temps de l'ordre de 25min à 1h
    la creation d'un dataset plus important aurait été encore plus long, il a été
    decidé de crée plusieur dataset afin de pouvoir exploiter un dataset pendant
    la création d'un autre.

PythonDataset
-------------
    Creation de bucket

QuestionWithStacktraceDataset
-----------------------------
    #Nombre de questions
        //Question
        Resultat: 12492
    #Nombre de questions contenant une reponse acceptée
        //Question[AcceptedAnswer/Score]
        Resultat: 7167
    #Nombre de questions ne contenant pas de reponse acceptée mais ayant des reponse
        //Question[not(AcceptedAnswer/Score) and Reponses//Score]
        Resultat: 3796
    #Nombre de questions ne contenant pas de reponse acceptée mais ayant des reponse avec un score positif
        //Question[not(AcceptedAnswer/Score) and Reponses//Score > 0] 
        Resultat: 2283
    #Nombre de questions contenant une reponse acceptée mais ayant des reponse avec un score plus élevé
        //Question[Reponses//Score  > AcceptedAnswer/Score]
        Resultat: 713
    #Nombre de reponse contenant une stacktrace
        //Question[Reponses//Stack]]
        Resultat: 99

QuestionWithoutStacktraceDataset
--------------------------------
    #Nombre de questions
        //Question
        Resultat: 345217
    #Nombre de questions contenant une reponse acceptée
        //Question[AcceptedAnswer/Score]
        Resultat: 215288
    #Nombre de questions ne contenant pas de reponse acceptée mais ayant des reponse
        //Question[not(AcceptedAnswer/Score) and Reponses//Score]
        Resultat: 98154
    #Nombre de questions ne contenant pas de reponse acceptée mais ayant des reponse avec un score positif
        //Question[not(AcceptedAnswer/Score) and Reponses//Score > 0] 
        Resultat: 62907
    #Nombre de questions contenant une reponse acceptée mais ayant des reponse avec un score plus élevé
        //Question[Reponses//Score  > AcceptedAnswer/Score]
        Resultat: 29485
    #Nombre de reponse contenant une stacktrace
        //Question[Reponses//Stack]]
        Resultat: 626
    #Nombre de question ne contenant pas de reponse
        //Question[not(Reponses/Reponse)]
        Resultat: 131175

Sommaire du rapport
-------------------
Introduction

Developpement

    Reconnaissance de stacktrace python
    
        regex
        
    Stackoverflow datadump
    
        readme de posts.xml
        
    Parser crash dataset
    
        algo pseudo code
        
    Parser question avec crash
    
        algo pseudo code
        
    Parser question sans crash
    
        algo pseudo code
        
Exploitation des datasets

    Bucket & Xbase & Xpath
    
    CrashDataset
    
        Creation des buckets
        
            algo pseudo code
            
    Question avec crash
    
        Requete xpath
        
    Question sans crash
    
        Requete xpath
        
Interpretation des resultats

    Stat CrashDataset
    
    Stat Question avec crash
    
    Stat Question sans crash
    
Conclusion

