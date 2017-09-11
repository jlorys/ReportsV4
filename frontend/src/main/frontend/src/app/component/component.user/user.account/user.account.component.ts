import {Component, EventEmitter, Input, OnDestroy, Output} from "@angular/core";
import {Message} from "primeng/primeng";
import {ActivatedRoute, Router} from "@angular/router";
import {AppUser} from "../../component.admin/users/user";
import {Report} from "../../component.admin/report/report";
import {UserAccountDataService} from "./user.data.service";

@Component({
  templateUrl: 'user.account.component.html',
  selector: 'usersAccount',
})
export class UserAccountComponent implements OnDestroy {

  @Input() header = "Sprawozdania użytkownika...";
  user : AppUser;
  userReports: Report[] = [];

  private params_subscription: any;

  @Input() sub : boolean = false;
  @Output() onSaveClicked = new EventEmitter<AppUser>();

  msgs: Message[] = [];

  constructor(private route: ActivatedRoute, private router: Router, private userAccountDataService: UserAccountDataService) {
    if (this.sub) {
      return;
    }

    this.params_subscription = this.route.params.subscribe(params => {
      console.log('Constructor for users-account ');

        this.userAccountDataService.getLoggedUser()
          .subscribe(user => {
              this.user = user;
              this.userReports = user.reports;

            },
            error => this.msgs.push({severity:'error', summary:'Constructor error', detail: error})
          );

    });
  }

  ngOnDestroy() {
    if (!this.sub) {
      this.params_subscription.unsubscribe();
    }
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/userReports', id]);
  }

  changePassword: boolean = false;
  wantChangePassword(){
    this.changePassword = true;
  }

  oldPassword: string;
  newPassword: string;
  newPasswordRepeat: string;
  onChangePassword(){
    this.msgs = []; //this line fix disappearing of messages

    this.userAccountDataService.changePassword(this.oldPassword, this.newPassword, this.newPasswordRepeat).
    subscribe(
      user => {
        this.user = user;
        this.msgs = []; //this line fix disappearing of messages
        if (this.sub) {
          this.onSaveClicked.emit(this.user);
        } else {
          this.msgs.push({severity:'info', summary:'Zmieniono hasło', detail: 'OK!'})

        }
      },
      error => this.msgs.push({severity:'error', summary:'Nie można zmienić hasła', detail: 'OK!'})
    );
  }
}
