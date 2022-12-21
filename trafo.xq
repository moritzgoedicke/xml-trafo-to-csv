declare variable $path as xs:string external;
declare variable $file := doc($path);


declare variable $analogValue := $file//attribute::*[contains(., 'ObjectId:AnalogValue')]/..;
declare variable $analogInput := $file//attribute::*[contains(., 'ObjectId:AnalogInput')]/..;

declare variable $timestamp := data($file/treeview/node/@val);

declare variable $date := substring($timestamp, 18, 10);
declare variable $time := substring($timestamp, 29, 8);


<root>
<analogValue>
  {
    for $value in $analogValue
    let $id := replace(data($value/node[contains(@tag, "ObjectId")]/@val), "[\(analog\-value, \)]", "")
    order by xs:integer($id) ascending
    return
      <item>
        <Index>AV-{$id}</Index>
        <Obj.Type>AnalogValue</Obj.Type>
        <Object_Id>{$id}</Object_Id>
        <Obj.Name>{data($value/node[contains(@tag, "ObjectName")]/@val)}</Obj.Name>
        <Value>{data($value/node[contains(@tag, "PresentValue")]/@val)}</Value>
        <Description></Description>
        <Datum>{$date}</Datum>
        <Uhrzeit>{$time}</Uhrzeit>
      </item>
  }
  </analogValue>
  <analogInput>
  {
    for $value in $analogInput
    let $id := replace(data($value/node[contains(@tag, "ObjectId")]/@val), "[\(analog\-input, \)]", "")
    order by xs:integer($id) ascending
    return
      <item>
        <Index>AI-{$id}</Index>
        <Obj.Type>AnalogInput</Obj.Type>
        <Object_Id>{$id}</Object_Id>
        <Obj.Name>{data($value/node[contains(@tag, "ObjectName")]/@val)}</Obj.Name>
        <Value>{data($value/node[contains(@tag, "PresentValue")]/@val)}</Value>
        <Description></Description>
        <Datum>{$date}</Datum>
        <Uhrzeit>{$time}</Uhrzeit>
      </item>
  }
  </analogInput>
</root>