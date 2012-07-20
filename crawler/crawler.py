import urllib
from HTMLParser import HTMLParser
from urlparse import urlparse, parse_qs
import json

ROOT_URL = "http://www.seg-social.es"
comunidades = [] 

def getUrl(url):
    url = "%s%s" % (ROOT_URL, url)
    f = urllib.urlopen(url)
    s = f.read()
    f.close()

    s = s.decode("utf-8")
    return s

def getAttr(attrs, name):
    return map(lambda (k,v): v, filter(lambda(k,v): k == name, attrs))[0]

def parseComunidadAttrs(attrs):
    data = parseAttrs(attrs, "Comu")
    if data is None:
        return None
    else:
        c = Comunidad()
        c.url  = data[0]
        c.code = data[1]
        return c

def parseProvincias(attrs):
    data = parseAttrs(attrs, "prov")
    if data is None:
        return None
    else:
        c = Provincia()
        c.url  = data[0]
        c.code = data[1]
        return c

def parseLocalidades(attrs):
    data = parseAttrs(attrs, "loc")
    if data is None:
        return None
    else:
        c = Localidad()
        c.url  = data[0]
        c.code = data[1]
        return c


def parseAttrs(attrs, qs):
    href = map(lambda(k,v): v if k == "href" and v.find(qs+"=") > 0 else None, attrs)
    href = filter( lambda url: not url is None ,href)
    href = map( lambda url:  (url,
        parse_qs(urlparse(url).query)[qs][0].strip()), href)
    if len(href) ==  0 :
        return None
    else:
        return href[0]


class Comunidad:
    def __init__(self):
        self.code = ""
        self.name = ""
        self.url  = ""
        self.provincias = []
class Provincia:
    def __init__(self):
        self.code = ""
        self.name = ""
        self.url  = ""
        self.localidades = []

class Localidad:
    def __init__(self):
        self.code = ""
        self.name = ""
        self.url  = ""
        self.dias_festivos = []

class Festivitat:
    def __init__(self):
        self.dia = ""
        self.mes = ""
        self.tipo_festividad = ""

class ParseComunidaded(HTMLParser):
    def __init__(self):
        HTMLParser.__init__(self)
        self.comunidad = None

    def handle_starttag(self, tag, attrs):
        if tag == "a":
            self.comunidad = parseComunidadAttrs(attrs)

    def handle_endtag(self, tag):
        if not self.comunidad is None:
            comunidades.append(self.comunidad)
            self.comunidad = None

    def handle_data(self, data):
        if not self.comunidad is None:
            self.comunidad.name += data

class ParseProvincia(HTMLParser):
    def __init__(self, comunidad):
        HTMLParser.__init__(self)
        self.comunidad = comunidad

        self.current_provincia = None
        self.inside_provincias = False

    def handle_starttag(self, tag, attrs):
        if tag == "div" and any(map(lambda (k,v): k=="class" and v ==
            "lstProvincias", attrs)):
            self.inside_provincias = True

        if self.inside_provincias:
            self.current_provincia = parseProvincias(attrs)

    def handle_endtag(self, tag):
        if tag == "div":
            self.inside_provincias = True

        if tag == "a" and not self.current_provincia is None:
            self.comunidad.provincias.append(self.current_provincia)
            self.current_provincia = None


    def handle_data(self, data):
        if not self.current_provincia is None:
            self.current_provincia.name += data

class ParseLocalidad(HTMLParser):
    def __init__(self, provincia):
        HTMLParser.__init__(self)
        self.provincia = provincia 
        self.current_localidad= None
        self.inside= False

    def handle_starttag(self, tag, attrs):
        if tag == "div" and any(map(lambda (k,v): k=="id" and v ==
            "calendarioLstLocalidades", attrs)):
            self.inside= True

        if self.inside :
            self.current_localidad = parseLocalidades(attrs)

    def handle_endtag(self, tag):
        if tag == "div":
            self.inside = True

        if tag == "a" and not self.current_localidad is None:
            self.provincia.localidades.append(self.current_localidad)
            self.current_localidad= None


    def handle_data(self, data):
        if not self.current_localidad is None:
            self.current_localidad.name += data

class ParseCalendari(HTMLParser):
    def __init__(self, localidad):
        HTMLParser.__init__(self)
        self.current_month = None
        self.current_festivitat = None
        self.current_festivitat_type = None
        self.current_weekday = None
        self.current_day = None
        self.localidad = localidad

    def handle_starttag(self, tag, attrs):
        if tag == "table":
            containsSummary = any(map( lambda(k,v): k =="summary", attrs))
            if containsSummary:
                self.current_month = getAttr(attrs, "summary")
        if tag == "td":
            containsHeader = any(map(lambda(k,v): k =="headers", attrs))
            if containsHeader:
                self.current_weekday = getAttr(attrs, "headers")
        if tag == "strong" and not self.current_weekday is None:
            self.current_festivitat = getAttr(attrs, "title")
            self.current_festivitat_type = getAttr(attrs, "class")

    def handle_endtag(self, tag):
        if tag == "table":
            self.current_month = None
        if tag == "td": 
            
            if not self.current_festivitat is None:
                f = Festivitat()
                f.dia = self.current_day
                f.mes = self.current_month
                f.tipo_festividad = self.current_festivitat_type
                self.localidad.dias_festivos.append(f)
                #print self.current_month, self.current_weekday, self.current_festivitat, self.current_day

            self.current_weekday = None
            self.current_festivitat = None
            self.current_festivitat_type = None
            self.current_day =  None


    def handle_data(self, data):
        self.current_day = data

## Start parsing...
html = getUrl("/Internet_1/Masinformacion/CalendarioLaboral/index.htm")
parser = ParseComunidaded()
parser.feed( html )

for comunidad in comunidades:
    print comunidad.code, comunidad.name
    html = getUrl(comunidad.url)

    parser = ParseProvincia(comunidad)
    parser.feed(html)

    for prov in comunidad.provincias:
        print prov.code, prov.name, prov
        html = getUrl(prov.url)

        parser = ParseLocalidad( prov)
        parser.feed(html)

        for loc in prov.localidades:
            print loc.code, loc.name

            html = getUrl(loc.url)
            parser = ParseCalendari(loc)
            parser.feed(html)
            
            p = [{"day": x.dia, "month": x.mes, "type": x.tipo_festividad} for x in
                    loc.dias_festivos]
            p = json.dumps(p, indent=4)
            f = open("json/%s.json" % loc.code , "w")
            f.write(p)
            f.close()

# Escribin la jerarquia de comunitats, prov i localitats
p = [ {"name": x.name, "code": x.code, "prov": 
        [{"name": y.name, "code": y.code, "local": 
            [{"name": z.name, "code": z.code} for z in y.localidades]
        } for y in x.provincias]
    } for x in comunidades]
f = open("json/comunidades.json", "w")
f.write(json.dumps(p, sort_keys=True, indent= 4))
f.close()
