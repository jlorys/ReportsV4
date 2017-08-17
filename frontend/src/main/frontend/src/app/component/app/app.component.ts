import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {MenuItem} from 'primeng/primeng';
import {AuthService} from '../../service/auth.service';
import {Message} from 'primeng/primeng';
import {AppUserDataService} from "../users/user.data.service";
import {AppUser} from "../users/user";
import {Router} from "@angular/router";

@Component({
  selector: 'app-main',
  template: `
  <p-growl [value]="msgs"></p-growl>
        <div class="ui-g layout">
            <div class="ui-g-12 ui-md-1" style="background-color: #27384a">
             <a *ngIf="authenticated" md-mini-fab (click)="userInfo()" color="primary"><md-icon>accessibility</md-icon></a>
            </div>
            <div class="ui-g-12 ui-md-11 ui-g-nopad">
                <div class="ui-g-12 ui-g-nopad">
                    <p-menubar [model]="items"></p-menubar>
                </div>
                <div class="ui-g-12" style="height: 1900px;">
                    <router-outlet></router-outlet>        
                </div>
                <div class="ui-g-12" style="text-align: center;">
                    <i class="fa fa-github-alt"></i> <a href="https://github.com/lork93">https://github.com/lork93</a>
                </div>
            </div>
        </div>
        <p-dialog header="Proszę o zalogowanie" [visible]="displayLoginDialog" [responsive]="true" showEffect="fade" [modal]="true" [closable]="false" *ngIf="!authenticated">
            <p>Przy użyciu próbnej bazy ustawiamy admin/admin</p>
            <div ngForm class="ui-g">
                <div class="ui-g-12">
                    <div class="ui-g-4">
                        <label for="username">Username</label>
                    </div>
                    <div class="ui-g-8">
                        <input pInputText id="username" [(ngModel)]="username" name="username"/>
                    </div>
                </div>
                <div class="ui-g-12">
                    <div class="ui-g-4">
                        <label for="password">Hasło</label>
                    </div>
                    <div class="ui-g-8">
                        <input type="password" pPassword id="password" [(ngModel)]="password" name="password"/>
                    </div>
                </div>
            </div>
            <footer>
                <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
                    <button pButton (click)="login()" icon="fa-sign-in" label="Zaloguj się"></button>
                </div>
            </footer>
        </p-dialog>
   `,
  styles: [`
        .layout div {
            background-color: white;
            border: 1px solid #f5f7f8;
        }
    `]
})
export class AppComponent implements OnInit {
  private items: MenuItem[];

  msgs: Message[] = [];

  displayLoginDialog: boolean = false;
  loginFailed: boolean = false;
  authenticated: boolean = false;
  username: string = "admin";
  password: string = "admin";
  loggedUser: AppUser = new AppUser;

  constructor(private router: Router, private authService: AuthService, private appUserDataService: AppUserDataService) {
  }

  ngOnInit() {
    this.items = [
      {label: 'Strona główna', routerLink: ['/'], icon: 'fa-home'},

      {
        label: 'Sprawozdania',
        icon: 'fa-file-text-o',
        items: [
          {label: 'Wyszukiwanie Sprawozdania', icon: 'fa-search', routerLink: ['/reports']},
          {label: 'Tworzenie Sprawozdania', icon: 'fa-gavel', routerLink: ['/reports/add']},
        ]
      },

      {
        label: 'Użytkownicy',
        icon: 'fa-user-circle',
        items: [
          {label: 'Wyszukiwanie Użytkownika', icon: 'fa-search', routerLink: ['/users']},
          {label: 'Tworzenie Użytkownika', icon: 'fa-gavel', routerLink: ['/users/add']},
        ]
      },

      {
        label: 'Role',
        icon: 'fa-universal-access',
        items: [
          {label: 'Wyszukiwanie Ról', icon: 'fa-search', routerLink: ['/roles']},
          {label: 'Tworzenie Ról', icon: 'fa-gavel', routerLink: ['/roles/add']},
        ]
      },

      {
        label: 'Laboratoria',
        icon: 'fa-columns',
        items: [
          {label: 'Wyszukiwanie Laboratoriów', icon: 'fa-search', routerLink: ['/laboratories']},
          {label: 'Tworzenie Laboratoriów', icon: 'fa-gavel', routerLink: ['/laboratories/add']},
        ]
      },

      {
        label: 'Przemioty',
        icon: 'fa-square',
        items: [
          {label: 'Wyszukiwanie Przemiotów', icon: 'fa-search', routerLink: ['/subjects']},
          {label: 'Tworzenie Przedmiotów', icon: 'fa-gavel', routerLink: ['/subjects/add']},
        ]
      },

      {
        label: 'Kierunki studiów',
        icon: 'fa-arrows',
        items: [
          {label: 'Wyszukiwanie kierunków', icon: 'fa-search', routerLink: ['/fieldsofstudies']},
          {label: 'Tworzenie kierunków', icon: 'fa-gavel', routerLink: ['/fieldsofstudies/add']},
        ]
      },

      {
        label: 'Wykresy',
        icon: 'fa-bar-chart',
        items: [
          {label: 'Wykres kołowy', routerLink: ['/doughnut']},
          {label: 'Diagram czasu', routerLink: ['/schedule']},
        ]
      },

      {
        label: 'Dokumentacja',
        icon: 'fa-book',
        items: [
          {label: "Angular Cli", icon: 'fa-external-link', url: "https://cli.angular.io/"},
          {label: "Angular 4", icon: 'fa-external-link', url: "https://angular.io/"},
          {label: "Material 2", icon: 'fa-external-link', url: "https://material.angular.io/"},
          {label: "PrimeNG Showcase", icon: 'fa-external-link', url: "http://www.primefaces.org/primeng"},
          {label: "TypeScript", icon: 'fa-external-link', url: "https://www.typescriptlang.org/"},
          {label: "Spring Boot", icon: 'fa-external-link', url: "http://projects.spring.io/spring-boot/"},
          {label: "Spring Data JPA", icon: 'fa-external-link', url: "http://projects.spring.io/spring-data-jpa/"}
        ]
      }
    ];

    this.authService.isAuthenticated().subscribe(
      resp => {
        this.authenticated = resp;
        this.displayLoginDialog = !this.authenticated;
        this.msgs = []; //this line fix disappearing of messages
        if (this.authenticated) {
          this.items.push({label: 'Wyloguj się', url: '/api/logout', icon: 'fa-sign-out'});
          console.log('You are authenticated...', '');
        } else {
          console.log('You are NOT authenticated...', '');
        }
      },
      error => this.msgs.push({severity:'error', summary:'isAuthenticated Error', detail: error})
    );
  }

  login() {
    this.authService.login(this.username, this.password).subscribe(
      loginOk => {
        if (loginOk) {
          this.displayLoginDialog = false;
          this.authenticated = true;
          this.items.push({label: 'Wyloguj się', url: '/api/logout', icon: 'fa-sign-out'});
          this.loginFailed = false;
          this.msgs = []; //this line fix disappearing of messages
          this.msgs.push({severity:'info', summary:'Jesteś teraz zalogowany', detail: ""});
        } else {
          this.loginFailed = true;
          this.displayLoginDialog = true;
          this.authenticated = false;
        }
      },
      error => {
        this.msgs = []; //this line fix disappearing of messages
        this.msgs.push({severity:'error', summary:'Login error', detail: error});
        this.loginFailed = true;
        this.displayLoginDialog = true;
        this.authenticated = false;
      }
    );
  }

  userInfo() {
    this.appUserDataService.getUserByUserName(this.username).subscribe(
      pageResponse => {
        this.loggedUser = pageResponse;
        this.msgs = []; //this line fix disappearing of messages
        this.msgs.push({severity:'info', summary:'Zalogowany użytkownik: ', detail: 'username: ' + this.loggedUser.userName
        + ' imię: ' + this.loggedUser.firstName + ' nazwisko: ' + this.loggedUser.lastName});
      },
      error => this.msgs.push({severity:'error', summary:'Nie dało się otrzymać wyników', detail: error})
    )
  }

  // sample method from angular doc
  private handleError(error: any) {
    let errMsg = (error.message) ? error.message :
      error.status ? `Status: ${error.status} - Text: ${error.statusText}` : 'Server error';
    console.error(errMsg); // log to console instead
    return Observable.throw(errMsg);
  }
}
