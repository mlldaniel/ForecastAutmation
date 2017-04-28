Option Explicit

Dim objFSO, strFileIn, strFileOut

With WScript.Arguments
	If .Named.Count > 0 Then
		Syntax
	End If
	Select Case .Unnamed.Count
		Case 1
			Set objFSO = CreateObject( "Scripting.FileSystemObject" )
			strFileIn  = WScript.Arguments(0)
			strFileOut = objFSO.GetBaseName( strFileIn ) & "_ascii.txt"
			Set objFSO = Nothing
		Case 2
			strFileIn  = WScript.Arguments(0)
			strFileOut = WScript.Arguments(1)
		Case Else
			Syntax
	End Select
End With

ASCII strFileIn, strFileOut


Function ASCII( myFileIn, myFileOut )
' ASCII()  Version 1.00
' Open a "plain" text file and save it again in US ASCII format
' (overwriting an existing file without asking for confirmation).
'
' Based on a sample script from JTMar:
' http://bytes.com/groups/asp/52959-save-file-utf-8-format-asp-vbscript
'
' Written by Rob van der Woude
' http://www.robvanderwoude.com

	Dim objStream, strText

	' Valid Charset values for ADODB.Stream
	Const CdoBIG5        = "big5"
	Const CdoEUC_JP      = "euc-jp"
	Const CdoEUC_KR      = "euc-kr"
	Const CdoGB2312      = "gb2312"
	Const CdoISO_2022_JP = "iso-2022-jp"
	Const CdoISO_2022_KR = "iso-2022-kr"
	Const CdoISO_8859_1  = "iso-8859-1"
	Const CdoISO_8859_2  = "iso-8859-2"
	Const CdoISO_8859_3  = "iso-8859-3"
	Const CdoISO_8859_4  = "iso-8859-4"
	Const CdoISO_8859_5  = "iso-8859-5"
	Const CdoISO_8859_6  = "iso-8859-6"
	Const CdoISO_8859_7  = "iso-8859-7"
	Const CdoISO_8859_8  = "iso-8859-8"
	Const CdoISO_8859_9  = "iso-8859-9"
	Const cdoKOI8_R      = "koi8-r"
	Const cdoShift_JIS   = "shift-jis"
	Const CdoUS_ASCII    = "us-ascii"
	Const CdoUTF_7       = "utf-7"
	Const CdoUTF_8       = "utf-8"

	' ADODB.Stream file I/O constants
	Const adTypeBinary          = 1
	Const adTypeText            = 2
	Const adSaveCreateNotExist  = 1
	Const adSaveCreateOverWrite = 2

	On Error Resume Next
	
	Set objStream = CreateObject( "ADODB.Stream" )
	objStream.Open
	objStream.Type = adTypeText
	objStream.Position = 0
	objStream.LoadFromFile myFileIn
	strText = objStream.ReadText
	objStream.Close

	objStream.Open
	objStream.Type = adTypeText
	objStream.Position = 0
	objStream.Charset = CdoUS_ASCII
	objStream.WriteText strText
	objStream.SaveToFile myFileOut, adSaveCreateOverWrite
	objStream.Close
	Set objStream = Nothing
	
	If Err Then
		ASCII = False
	Else
		ASCII = True
	End If
	
	On Error Goto 0
End Function


Sub Syntax( )
	Dim strMsg
	strMsg = vbCrLf _
	       & "Txt2ASCI.vbs,  Version 1.00" & vbCrLf _
	       & "Convert any ""plain"" text file into US ASCII format" _
	       & vbCrLf & vbCrLf _
	       & "Usage:  TXT2ASCI.VBS  input_file  [ output_file ]" _
	       & vbCrLf & vbCrLf _
	       & "Where:  input_file   is the text file to be converted" _
	       & vbCrLf _
	       & "        output_file  is the ASCII text file to be created" _
	       & vbCrLf _
	       & "                     (default: input_file basename with ""_ascii.txt"" appended)" _
	       & vbCrLf & vbCrLf _	       
	       & "Notes:  If output_file exists, it will be overwritten without notification." _
	       & vbCrLf _
	       & "        This script has only minimal error handling, it does not check if" _
	       & vbcrlf _
	       & "        files exist." _
	       & vbCrLf & vbCrLf _
	       & "Written by Rob van der Woude" & vbCrLf _
	       & "http://www.robvanderwoude.com" & vbCrLf
	WScript.Echo strMsg
	WScript.Quit 1
End Sub
